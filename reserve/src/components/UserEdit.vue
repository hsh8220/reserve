<template>
  <v-layout row class="white">
    <v-flex xs12 sm6 offset-sm3>
      <v-card flat>
        <v-toolbar color="transparent" card>
          <v-toolbar-title><h2>사용자 편집</h2></v-toolbar-title>
          <v-spacer></v-spacer>
          <span>총 {{users.length}}명</span>
        </v-toolbar>
        <v-list dense>
          <v-list-tile
            v-for="user in users"
            :key="user.id"
            @click=""
          >
            <v-list-tile-avatar>
              <v-icon v-if="user.role == 'MANAGER'" color="pink">supervisor_account</v-icon>
            </v-list-tile-avatar>
            <v-list-tile-content>
              <v-list-tile-title v-text="user.name"></v-list-tile-title>
            </v-list-tile-content>
            <v-list-tile-action>
              <v-btn slot="activator" flat small color="red lighten-2" @click="edit(user)">편집</v-btn>
            </v-list-tile-action>
          </v-list-tile>
        </v-list>
        <v-dialog v-model="set_dialog">
          <v-card>
            <v-card-title
              class="headline grey lighten-2"
              primary-title
            >
              사용자 편집
            </v-card-title>

            <v-card-text>
              <v-form ref="form" v-model="valid" lazy-validation>
                <v-text-field
                  v-model="name"
                  :rules="[v => !!v || '이름을 입력하세요.']"
                  :counter="10"
                  label="사용자 이름"
                  required
                ></v-text-field>
                <v-select
                  v-model="gender"
                  :items="genders"
                  :rules="[v => !!v || '성별을 선택하세요.']"
                  label="성별"
                  required
                ></v-select>
                <v-select
                  v-model="role"
                  :items="roles"
                  :rules="[v => !!v || '권한을 선택하세요.']"
                  label="사용자 권한"
                  required
                ></v-select>
              </v-form>
            </v-card-text>

            <v-divider></v-divider>

            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn
                :disabled="!valid"
                color="primary"
                @click="update"
              >
                수정
              </v-btn>
              <v-btn color="red lighten-2" dark @click="remove(id)">삭제</v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-card>
    </v-flex>
  </v-layout>
</template>

<script>
  export default {
    name: "UserEdit",
    data: () => ({
      users: [],
      set_dialog: false,
      valid: true,
      progress: false,
      id: '',
      pw: '',
      name: '',
      fcmToken: '',
      role: null,
      gender: null,
      roles: [
        'BASIC',
        'MANAGER',
      ],
      genders: [
        '남자',
        '여자'
      ],
    }),
    created: function () {
      this.getUsers()
    },
    methods: {
      getUsers: function () {
        this.$http.get('/api/user/congregation/' + sessionStorage.congregationId)
          .then(data => {
            if (data.data) {
              this.users = data.data.filter(x => x.role != 'ADMIN')
              this.progress = false
            } else {
              this.progress = false
              alert("사용 기간이 만료되었습니다. 다시 로그인해 주세요.")
              this.$router.push('/login')
            }
          })
      },
      edit: function (user) {
        this.id = user.id
        this.pw = user.pw
        this.name = user.name
        this.role = user.role
        this.gender = user.gender
        this.fcmToken = user.fcmToken
        this.set_dialog = true
      },
      update: function () {
        if (this.valid) {
          this.$http.put('/api/user', {
            id: this.id,
            userId: this.name,
            pw: this.pw,
            name: this.name,
            role: this.role,
            gender: this.gender,
            congregation: {'id':sessionStorage.congregationId, 'name':sessionStorage.congregationName},
            fcmToken: this.fcmToken
          }).then(data => {
            if (data.data.result == "error") {
              alert("오류가 발생하였습니다.")
            } else {
              alert("저장하였습니다.")
              this.getUsers()
              this.set_dialog = false
            }
          })
        }
      },
      remove: function (id) {
        this.$http.delete('/api/user/' + id)
          .then(data => {
            if (data.data.result == "error") {
              alert("오류가 발생하였습니다.")
            } else {
              alert("삭제하였습니다.")
              this.getUsers()
              this.set_dialog = false
            }
          })
      }
    }
  }
</script>

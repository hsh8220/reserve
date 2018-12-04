<template>
  <v-layout row class="white">
    <v-flex xs12 sm6 offset-sm3>
      <v-card flat>
        <v-toolbar color="transparent" card>
          <v-toolbar-title><h2>전시대 편집</h2></v-toolbar-title>
        </v-toolbar>
        <v-list dense>
          <v-list-tile
            v-for="spot in spots"
            :key="spot.id"
            @click=""
          >
            <v-list-tile-action>
              <v-icon color="pink">place</v-icon>
            </v-list-tile-action>
            <v-list-tile-content>
              <v-list-tile-title v-text="spot.name"></v-list-tile-title>
            </v-list-tile-content>
            <v-dialog v-model="set_dialog">
              <v-btn slot="activator" flat small color="red lighten-2" @click="edit(spot)">편집</v-btn>
              <v-card>
                <v-card-title
                  class="headline grey lighten-2"
                  primary-title
                >
                  전시대 편집
                </v-card-title>

                <v-card-text>
                  <v-form ref="form" v-model="valid" lazy-validation>
                    <v-text-field
                      v-model="name"
                      :rules="rules"
                      :counter="10"
                      label="전시대"
                      required
                    ></v-text-field>
                    <v-text-field
                      v-model="guide"
                      :rules="rules"
                      :counter="10"
                      label="인도자"
                      required
                    ></v-text-field>
                    <v-text-field
                      v-model="time"
                      :rules="rules"
                      label="시간"
                      required
                    ></v-text-field>
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
                    저장
                  </v-btn>
                  <v-btn color="red lighten-2" dark @click="remove(spot.id)">삭제</v-btn>
                </v-card-actions>
              </v-card>
            </v-dialog>
          </v-list-tile>
        </v-list>
      </v-card>
    </v-flex>
  </v-layout>
</template>

<script>
  export default {
    name: "SpotEdit",
    data: () => ({
      spots: [],
      progress: false,
      set_dialog: false,
      valid: true,
      id: '',
      name: '',
      guide: '',
      time: '',
      rules: [
        v => !!v || '항목을 입력하세요.'
      ]
    }),
    created: function () {
      this.getSpots();
    },
    methods: {
      getSpots: function () {
        this.$http.get('/api/exhibition/')
          .then(data => {
            if (data.data) {
              this.spots = data.data
              this.progress = false
            } else {
              this.progress = false
              alert("사용 기간이 만료되었습니다. 다시 로그인해 주세요.")
              this.$router.push('/login')
            }
          })
      },
      edit: function (spot) {
        this.id = spot.id
        this.name = spot.name
        this.guide = spot.guide
        this.time = spot.time
      },
      update: function () {
        if (this.valid) {
          this.$http.put('/api/exhibition', {
            id: this.id,
            name: this.name,
            guide: this.guide,
            time: this.time
          }).then(data => {
            if (data.data.result == "error") {
              alert("오류가 발생하였습니다.")
            } else {
              alert("저장하였습니다.")
              this.getSpots()
              this.set_dialog = false
            }
          })
        }
      },
      remove: function (id) {
        this.$http.delete('/api/exhibition/' + id)
          .then(data => {
            if (data.data.result == "error") {
              alert("오류가 발생하였습니다.")
            } else {
              alert("삭제하였습니다.")
              this.getSpots()
              this.set_dialog = false
            }
          })
      }
    }
  }
</script>

<template>
  <v-layout row class="white">
    <v-flex xs12 sm6 offset-sm3>
      <v-card flat>
        <v-toolbar color="transparent" card>
          <v-toolbar-title><h2>사용자 편집</h2></v-toolbar-title>
        </v-toolbar>
        <v-list dense>
          <v-list-tile
            v-for="user in users"
            :key="user.id"
            @click=""
          >
            <v-list-tile-action>
              <v-icon color="pink">person</v-icon>
            </v-list-tile-action>
            <v-list-tile-content>
              <v-list-tile-title v-text="user.name"></v-list-tile-title>
            </v-list-tile-content>
            <!--<v-list-tile-action v-if="userId == state.member.userId && !isBefore">-->
              <!--<v-btn flat small color="error" @click="cancel(state.id)">신청 취소</v-btn>-->
            <!--</v-list-tile-action>-->
          </v-list-tile>
        </v-list>
      </v-card>
    </v-flex>
  </v-layout>
</template>

<script>
  export default {
    name: "UserEdit",
    data: () => ({
      users: [],
      progress: false
    }),
    created: function () {
      this.$http.get('/api/user/')
        .then(data => {
          if (data.data) {
            this.users = data.data
            this.progress = false
          } else {
            this.progress = false
            alert("사용 기간이 만료되었습니다. 다시 로그인해 주세요.")
            this.$router.push('/login')
          }
        })
    },
    methods: {
    }
  }
</script>

<template>
  <v-app>
    <v-content class="bg-img"></v-content>
    <v-content class="content">
      <v-container class="content" fluid fill-height>
        <v-layout align-center justify-center>
          <v-flex xs12 sm8 md4>
            <v-card class="elevation-12">
              <v-toolbar color="transparent" card>
                <v-toolbar-title><h2>전시대 신청</h2></v-toolbar-title>
                <v-spacer></v-spacer>
                v.2.0.0
              </v-toolbar>
              <v-card-text>
                <v-form>
                  <v-select prepend-icon="store" v-model="congregation" :items="congregations" item-text="name"
                            return-object label="회중을 선택하세요"></v-select>
                  <v-text-field prepend-icon="person" name="login" label="이름을 입력하세요" type="text"
                                v-model="userId"></v-text-field>
                  <!--<v-text-field id="password" prepend-icon="lock" name="password" label="패스워드" type="password"-->
                  <!--v-model="pw"></v-text-field>-->
                </v-form>
              </v-card-text>
              <v-card-actions>
                <v-btn block color="primary" @click="onSubmit(userId, pw, congregation)">로그인</v-btn>
                <!--<v-btn color="primary" v-on:click="join()">가입하기</v-btn>-->
              </v-card-actions>
            </v-card>
          </v-flex>
        </v-layout>
      </v-container>
    </v-content>
    <Loader :dialog="progress"></Loader>
  </v-app>
</template>

<script>
  export default {
    data() {
      return {
        userId: '',
        pw: '',
        msg: '',
        congregation: '',
        progress: false,
        congregations: []
      }
    },
    name: 'Login',
    created: function () {
      //localStorage에 데이터가 존재하는 경우 자동 완성 한다.
      if (localStorage.userId != undefined) {
        this.userId = localStorage.userId
        this.congregation = {'id': localStorage.congregationId, 'name': localStorage.congregationName}
      }
      this.progress = true
      this.$http.get('/api/congregation')
        .then(data => {
          if (data.data) {
            this.congregations = data.data
            this.progress = false
          } else {
            this.progress = false
          }
        })
    },
    methods: {
      join() {
        this.$router.push('join')
      },
      onSubmit(userId, pw, congregation) {
        this.progress = true
        this.$store.dispatch('LOGIN', {'userId': userId, 'pw': pw, 'congregation': congregation})
          .then(() => {
            this.progress = false
            this.sendFcmTokenToServer({'userId': userId, 'pw': pw, 'congregation': congregation})
            this.redirect(sessionStorage.role)
          })
          .catch(({message}) => {
            this.progress = false
            if (this.$store.getters.getResult == 'noID') alert('사용자가 없습니다.');
            this.$router.push('login')
          })
      },
      redirect(role) {
        if (this.$store.getters.getAccessToken != '') {
          this.$router.push('/home/spotList')
        }
      },
      sendFcmTokenToServer(member) {
        let that = this
        this.$messaging.getToken().then(function (refreshedToken) {
          console.log('Token refreshed.');
          that.$http.post('/api/fcmToken/'+refreshedToken, member)
            .then(data => {
              console.log('Token refreshed.');
            })
            .catch(function (err) {
              console.log('Token error.');
            })
        })
      }
    }
  }
</script>

<style>
  .bg-img {
    background-image: url("https://assetsnffrgf-a.akamaihd.net/assets/m/302016049/univ/art/302016049_univ_lsr_xl.jpg");
    background-position: center;
    background-size: cover;
    filter: blur(3px);
    -webkit-filter: blur(3px);
    -ms-filter: blur(3px);
    -moz-filter: blur(3px);
    -o-filter: blur(3px);

    position: fixed;
    left: 0;
    right: 0;
    height: 100%;
  }
</style>

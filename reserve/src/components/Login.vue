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
                v.1.3.1
              </v-toolbar>
              <v-card-text>
                <v-form>
                  <v-text-field prepend-icon="person" name="login" label="이름을 입력해 주세요" type="text"
                                v-model="userId"></v-text-field>
                  <!--<v-text-field id="password" prepend-icon="lock" name="password" label="패스워드" type="password"-->
                                <!--v-model="pw"></v-text-field>-->
                </v-form>
              </v-card-text>
              <v-card-actions>
                <v-btn block color="primary" @click="onSubmit(userId, pw)">로그인</v-btn>
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
        progress: false
      }
    },
    name: 'Login',
    methods: {
      join() {
        this.$router.push('join')
      },
      onSubmit(userId, pw) {
        this.progress = true
        this.$store.dispatch('LOGIN', {'userId': userId, 'pw': pw})
          .then(() => {
            this.progress = false
            this.redirect(sessionStorage.role)
          })
          .catch(({message}) => {
            this.msg = message
            this.progress = false
          })
      },
      redirect(role) {
        if (this.$store.getters.getAccessToken != '') {
          console.log(role)
          this.$router.push('/home/spotList')
        } else {
          if (this.$store.getters.getResult == 'noID') alert('사용자가 없습니다.');
          this.$router.push('login')
        }
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

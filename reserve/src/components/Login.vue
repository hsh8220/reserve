<template>
  <v-app>
    <v-content>
      <v-container fluid fill-height>
        <v-layout align-center justify-center>
          <v-flex xs12 sm8 md4>
            <v-card class="elevation-12">
              <v-toolbar dark color="primary">
                <v-toolbar-title>전시대 신청</v-toolbar-title>
                <v-spacer></v-spacer>
              </v-toolbar>
              <v-card-text>
                <v-form>
                  <v-text-field prepend-icon="person" name="login" label="이름을 입력해 주세요" type="text"
                                v-model="userId"></v-text-field>
                  <v-text-field id="password" prepend-icon="lock" name="password" label="패스워드" type="password"
                                v-model="pw"></v-text-field>
                </v-form>
              </v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="primary" v-on:click="onSubmit(userId, pw)">로그인</v-btn>
                <v-btn color="primary" v-on:click="join()">가입하기</v-btn>
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
            this.redirect()
          })
          .catch(({message}) => {
            this.msg = message
            this.progress = false
          })
      },
      redirect() {
        if (this.$store.getters.getAccessToken != '') {
          this.$router.push('/home/spotList')
        } else {
          if (this.$store.getters.getResult == 'noID') alert('아이디가 없습니다.');
          else if (this.$store.getters.getResult == 'noPW') alert('패스워드가 틀렸습니다.');
          this.$router.push('login')
        }
      }
    }
  }
</script>

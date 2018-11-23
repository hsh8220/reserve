<template>
  <v-app>
    <v-content>
      <v-container fluid fill-height>
        <v-layout align-center justify-center>
          <v-flex xs12 sm8 md3>
            <v-card class="elevation-12">
              <v-toolbar dark color="primary">
                <v-toolbar-title>가입 하기</v-toolbar-title>
                <v-spacer></v-spacer>
              </v-toolbar>
              <v-card-text>
                <v-form>
                  <v-text-field prepend-icon="person" name="login" label="아이디" type="text"
                                v-model="input.userId"></v-text-field>
                  <v-text-field id="password" prepend-icon="lock" name="password" label="패스워드" type="password"
                                v-model="input.pw"></v-text-field>
                  <v-text-field id="password" prepend-icon="person" name="password" label="이름" type="text"
                                v-model="input.name"></v-text-field>
                </v-form>
              </v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="primary" v-on:click="join()">가입하기</v-btn>
                <v-btn color="primary" v-on:click="back()">로그인 이동</v-btn>
              </v-card-actions>
            </v-card>
          </v-flex>
        </v-layout>
      </v-container>
    </v-content>
  </v-app>
</template>

<script>
export default {
  name: 'Join',
  data () {
    return {
      input: {
        userId: '',
        pw: '',
        name: ''
      }
    }
  },
  methods: {
    join () {
      if(this.validation()) {
        this.$http.post('/user', this.input)
          .then(res => {
            if(res.data.result == 'error') {
              alert('아이디가 중복됩니다.')
            }else {
              alert('로그인페이지로 이동합니다.')
              this.$router.push('login')
            }
          })
      }
    },
    back () {
      this.$router.go(-1)
    },
    validation () {
      if(this.input.userId && this.input.pw && this.input.name) {
        return true
      }else {
        alert("항목을 다시 확인하여 주세요.");
        return false
      }
    }
  }
}
</script>

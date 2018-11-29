<template>
  <v-layout row class="white">
    <v-flex xs12 sm6 offset-sm3>
      <v-card flat>
        <v-toolbar color="transparent" card>
          <v-toolbar-title><h2>전시대 추가</h2></v-toolbar-title>
        </v-toolbar>
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

            <v-btn
              :disabled="!valid"
              @click="submit"
              color="primary"
            >
              저장
            </v-btn>
            <v-btn @click="clear" color="primary">취소</v-btn>
          </v-form>
        </v-card-text>
      </v-card>
    </v-flex>
  </v-layout>
</template>

<script>
  export default {
    name: "SpotAdd",
    data: () => ({
      valid: true,
      name: '',
      guide: '',
      time: '',
      rules: [
        v => !!v || '항목을 입력하세요.'
      ]
    }),
    methods: {
      submit() {
        if (this.$refs.form.validate()) {
          this.$http.post('/api/exhibition', {
            name: this.name,
            guide: this.guide,
            time: this.time
          }).then(data => {
            if (data.data.result == "error") {
              alert("오류가 발생하였습니다.")
            }
          })
        }
      },
      clear() {
        this.$refs.form.reset()
      }
    }
  }
</script>

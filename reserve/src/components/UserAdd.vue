<template>
  <v-layout row class="white">
    <v-flex xs12 sm6 offset-sm3>
      <v-card flat>
        <v-toolbar color="transparent" card>
          <v-toolbar-title><h2>사용자 추가</h2></v-toolbar-title>
        </v-toolbar>
        <v-card-text>
          <v-form ref="form" v-model="valid" lazy-validation>
            <v-text-field
              v-model="name"
              :rules="nameRules"
              :counter="10"
              label="사용자 이름"
              required
            ></v-text-field>
            <v-select
              v-model="select"
              :items="items"
              :rules="[v => !!v || 'Item is required']"
              label="사용자 권한"
              required
            ></v-select>

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
    name: "UserAdd",
    data: () => ({
      valid: true,
      name: '',
      nameRules: [
        v => !!v || '이름을 입력하세요.'
      ],
      select: null,
      items: [
        'BASIC',
        'MANAGER',
      ],
    }),
    methods: {
      submit() {
        if (this.$refs.form.validate()) {
          this.$http.post('/api/user', {
            userId: this.name,
            name: this.name,
            role: this.select
          }).then(data => {
            if (data.data.result == "error") {
              alert("사용자 이름이 중복 됩니다.")
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

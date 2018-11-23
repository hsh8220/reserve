<template>
  <v-container>
    <v-layout>
      <v-flex xs12 sm6 offset-sm3>
        <div>
          <datepicker class="my-datepicker" calendar-class="my-datepicker_class" :inline="true"
                      :language="ko" v-model="date" @selected="selectedDate"
          ></datepicker>
        </div>
        <span id="test"></span>
        <v-divider style="margin-bottom: 20px; margin-top: 20px"></v-divider>

        <v-card>
          <v-list>
            <v-list-tile
              v-for="state in states"
              :key="state.id"
            >
              <v-list-tile-action>
                <v-icon color="pink">person</v-icon>
              </v-list-tile-action>
              <v-list-tile-content>
                <v-list-tile-title v-text="state.member.name"></v-list-tile-title>
              </v-list-tile-content>
            </v-list-tile>
          </v-list>
        </v-card>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
  import Datepicker from 'vuejs-datepicker'
  import {en, ko} from 'vuejs-datepicker/dist/locale'

  export default {
    name: "State",
    components: {Datepicker},
    data() {
      return {
        en: en,
        ko: ko,
        states: [],
        date: new Date()
      }
    },
    created: function () {
      let today = this.$moment(new Date()).format('YYYY-MM-DD')
      this.$http.get('/api/state/' + this.$route.params.id + '/' + today)
        .then(data => {
          if (data.data) {
            this.states = data.data
          } else {
            alert("사용 기간이 만료되었습니다. 다시 로그인해 주세요.")
            this.$router.push('/login')
          }
        })
    },
    methods: {
      selectedDate: function (val) {
        let date = this.$moment(val).format('YYYY-MM-DD')

        this.$http.get('/api/state/' + this.$route.params.id + '/' + date)
          .then(data => {
            if (data.data) {
              this.states = data.data
            } else {
              alert("사용 기간이 만료되었습니다. 다시 로그인해 주세요.")
              this.$router.push('/login')
            }
          })
      }
    }
  }
</script>

<style scoped>
  .my-datepicker >>> .my-datepicker_class {
    width: 100%;

  }
</style>

<template>
  <v-container>
    <v-layout>
      <v-flex xs12 sm6 offset-sm3>
        <div>
          <v-date-picker v-model="date" locale="ko" full-width @input="selectedDate" :events="arrayEvents"
                         event-color="pink" :picker-date.sync="pickerDate"></v-date-picker>
        </div>
        <span id="test"></span>
        <v-divider class="divider"></v-divider>
        <v-subheader>총 {{states.length}}명</v-subheader>
        <v-btn v-if="!isBefore && !isReserved" class="reserve_button" block large round color="primary"
               @click="reserve">신청 하기
        </v-btn>
        <v-card v-if="states.length > 0">
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
              <v-list-tile-action v-if="userId == state.member.userId && !isBefore">
                <v-btn flat small color="error" @click="cancel(state.id)">신청 취소</v-btn>
              </v-list-tile-action>
            </v-list-tile>
          </v-list>
        </v-card>
        <div class="title" v-else>
          <v-icon large>error</v-icon>
          신청자가 존재하지 않습니다.
        </div>
      </v-flex>
    </v-layout>
    <Loader :dialog="progress"></Loader>
  </v-container>
</template>

<script>

  export default {
    name: "State",
    data() {
      return {
        states: [],
        date: this.$moment(new Date()).format('YYYY-MM-DD'),
        pickerDate: null,
        arrayEvents: [],
        userId: sessionStorage.userId,
        isBefore: false,
        isReserved: false,
        progress: false
      }
    },
    created: function () {
      this.progress = true
      let today = this.$moment(new Date()).format('YYYY-MM-DD')
      let month = this.$moment(new Date()).format('YYYY-MM')
      this.$http.get('/api/state/' + this.$route.params.id + '/' + today)
        .then(data => {
          if (data.data) {
            this.states = data.data
            this.isReserved = this.checkReserved();
            this.progress = false
          } else {
            this.progress = false
            alert("사용 기간이 만료되었습니다. 다시 로그인해 주세요.")
            this.$router.push('/login')
          }
        })
    },
    watch: {
      pickerDate (val) {
        this.viewCalendarState(val)
      }
    },
    methods: {
      selectedDate: function (date) {
        if (!this.progress) { //중복 클릭 방지 코드
          this.progress = true
          this.isBefore = this.$moment(date).isBefore(this.$moment(new Date()).format('YYYY-MM-DD'))

          this.$http.get('/api/state/' + this.$route.params.id + '/' + date)
            .then(data => {
              if (data.data) {
                this.states = data.data
                this.isReserved = this.checkReserved();
                this.progress = false
              } else {
                this.progress = false
                alert("사용 기간이 만료되었습니다. 다시 로그인해 주세요.")
                this.$router.push('/login')
              }
            })
        }
      },
      reserve: function () {
        if (!this.progress) {
          this.progress = true
          let date = this.$moment(this.date).format('YYYY-MM-DD')
          this.$http.post('/api/state/' + this.$route.params.id + '/' + date)
            .then(data => {
              this.viewCalendarState(this.pickerDate)
              this.progress = false
              this.selectedDate(this.date)
            })
        }
      },
      cancel: function (id) {
        if (!this.progress) {
          this.progress = true
          this.$http.delete('/api/state/' + id)
            .then(data => {
              this.viewCalendarState(this.pickerDate)
              this.progress = false
              this.selectedDate(this.date)
            })
        }
      },
      checkReserved: function () {
        let result = false
        this.states.forEach(function (state) {
          if (state.member.userId == sessionStorage.userId) result = true
        })
        return result
      },
      viewCalendarState:function (val) {
        this.$http.get('/api/state/month/' + this.$route.params.id + '/' + val) //달력에 신청자있는 날에 표시
          .then(data => {
            this.arrayEvents = data.data.map(state => this.$moment(state.startTime).format('YYYY-MM-DD'));
          })
      }
    }
  }
</script>

<style scoped>
  .divider {
    margin-bottom: 10px;
    margin-top: 10px
  }

  .reserve_button {
    margin-bottom: 20px;
  }
</style>

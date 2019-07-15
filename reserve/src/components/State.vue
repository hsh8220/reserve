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
        <v-subheader>총 {{states.length}} /<div class="pink--text" style="padding-left: 5px; padding-right: 5px">{{limitation}}</div>명
          <v-spacer></v-spacer>
          인도자 : {{exhibition.guide}}
        </v-subheader>
        <v-subheader v-if="timeRemain > 0 && timeRemain < 24 && isEventDay">
          신청가능한 시간이<span class="pink--text">{{timeRemain}}</span> 시간 남았습니다.
        </v-subheader>
        <v-subheader class="pink--text" v-else-if="timeRemain <= 0 && isEventDay">
          신청가능한 시간이 끝났습니다.
        </v-subheader>
        <v-alert v-if="states.length >= limitation" :value="true" type="info">더 이상 신청하실 수 없습니다.</v-alert>
        <v-btn v-else-if="isView" class="reserve_button" block large round color="primary"
               @click="reserve">{{applyButtonString}}
        </v-btn>
        <v-card v-if="states.length > 0">
          <v-list>
            <v-list-tile
              v-for="state in states"
              :key="state.id"
            >
              <v-list-tile-action>
                <v-icon v-if="state.member.gender == '여자'" color="pink">person</v-icon>
                <v-icon v-else color="primary">person</v-icon>
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
          전시대 마련이 없습니다.
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
        exhibition: {},
        limitation: 0,
        date: this.$moment(new Date()).format('YYYY-MM-DD'),
        pickerDate: '',
        arrayEvents: [],
        userId: sessionStorage.userId,
        isBefore: false,
        isReserved: false,
        progress: false,
        applyButtonString: '신청 하기',
        timeRemain: 0
      }
    },
    computed: {
      //인도자 여부 혹은 조건에 따라 신청 버튼 컨트롤
      isView: function () {
        let result = false;
        if (sessionStorage.role != 'BASIC') {
          if(this.states.length > 0 ) {
            this.applyButtonString = '신청 하기'
          }else {
            this.applyButtonString = '봉사 만들기'
          }
          if (!this.isBefore && !this.isReserved) result = true;
        } else {
          if (!this.isBefore && !this.isReserved && this.states.length > 0) result = true;
        }
        return result
      },
      isEventDay: function () {
        let rst = false;
        if(this.arrayEvents.filter(day => day == this.date).length > 0) rst = true;
        else rst = false;
        return rst;
      }
    },
    created: function () {
      this.progress = true
      let today = this.$moment(new Date()).format('YYYY-MM-DD')
      let month = this.$moment(new Date()).format('YYYY-MM')
      this.$http.get('/api/exhibition/' + this.$route.params.id)
        .then(data => {
          if (data.data) {
            this.exhibition = data.data
            this.limitation = this.exhibition.limitation
            this.isBefore = this.$moment().startOf('d').add(this.exhibition.timeLimit, 'h').isBefore(new Date())
            let t2 = this.$moment().startOf('d').add(this.exhibition.timeLimit, 'h')
            let t1 = this.$moment()
            this.timeRemain = Math.ceil(this.$moment.duration(t2.diff(t1)).asHours())
          } else {
            this.progress = false
            alert("사용 기간이 만료되었습니다. 다시 로그인해 주세요.")
            this.$router.push('/login')
          }
        })
        .then(data => {
          this.$http.get('/api/state/' + this.$route.params.id + '/' + today)
            .then(data => {
              if (data.data) {
                this.states = data.data
                this.isReserved = this.checkReserved()
                this.progress = false
              }
            })
        })
    },
    watch: {
      pickerDate(val) {
        this.viewCalendarState(val)
      }
    },
    methods: {
      selectedDate: function (date) {
        if (!this.progress) { //중복 클릭 방지 코드
          this.progress = true
          this.isBefore = this.$moment(date).add(this.exhibition.timeLimit, 'h').isBefore(new Date())
          let t2 = this.$moment(date).add(this.exhibition.timeLimit, 'h')
          let t1 = this.$moment()
          this.timeRemain = Math.ceil(this.$moment.duration(t2.diff(t1)).asHours())

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
      viewCalendarState: function (val) {
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

  .v-subheader {
    height: 30px;
  }
</style>

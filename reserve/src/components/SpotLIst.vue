<template>
  <v-container fluid grid-list-lg style="height: 200px">
    <v-layout row wrap>
      <v-flex xs12
              v-for="spot in spots"
              :key="spot.id"
              @click="selectSpot(spot.id, spot.guide)">
        <v-card color="grey lighten-2"
        >
          <v-card-text>
            <div class="display-1 text-xs-center black--text">{{spot.name}}</div>
            <p></p>
            <div class="title text-xs-center primary--text">{{spot.time}}</div>
          </v-card-text>
        </v-card>
      </v-flex>
    </v-layout>
    <Loader :dialog="progress"></Loader>
  </v-container>
</template>

<script>
  export default {
    name: 'SpotList',
    data: () => ({
      spots: [],
      progress: false
    }),
    created: function () {
      this.progress = true
      this.$http.get('/api/exhibition')
        .then(data => {
          if (data.data) {
            this.spots = data.data
            this.progress = false
          } else {
            this.progress = false
            alert("사용 기간이 만료되었습니다. 다시 로그인해 주세요.")
            this.$router.push('/login')
          }
        })
    },
    methods: {
      selectSpot: function (id, guide) {
        this.$router.push('/home/state/'+id+'/'+guide)
      }
    }
  }
</script>

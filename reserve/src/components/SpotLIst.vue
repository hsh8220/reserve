<template>
  <v-container fluid grid-list-lg style="height: 200px">
    <v-layout row wrap>
      <v-flex xs12
              v-for="spot in spots"
              :key="spot.id"
              @click="selectSpot(spot.id)">
        <v-card height="100px"
                class="white--text"
                color="blue-grey"
        >
          <v-card-text>
            <div class="display-1 text-xs-center">{{spot.name}}</div>
            <p class="title text-xs-center">{{spot.time}}</p>
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
      selectSpot: function (id) {
        this.$router.push('/home/state/'+id)
      }
    }
  }
</script>

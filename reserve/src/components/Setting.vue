<template>
  <v-layout row class="white">
    <v-flex xs12 sm6 offset-sm3>
      <v-list>
        <v-subheader>설정 항목</v-subheader>
        <v-list-group
          prepend-icon="people"
          no-action
        >
          <v-list-tile slot="activator">
            <v-list-tile-content>
              <v-list-tile-title>사용자 설정</v-list-tile-title>
            </v-list-tile-content>
          </v-list-tile>

          <v-list-tile>
            <v-list-tile-content @click="userAdd">
              <v-list-tile-title>사용자 추가</v-list-tile-title>
            </v-list-tile-content>
          </v-list-tile>
          <v-list-tile>
            <v-list-tile-content @click="userEdit">
              <v-list-tile-title>사용자 편집</v-list-tile-title>
            </v-list-tile-content>
          </v-list-tile>
        </v-list-group>
        <v-list-group
          prepend-icon="place"
          no-action
        >
          <v-list-tile slot="activator">
            <v-list-tile-content>
              <v-list-tile-title>전시대 설정</v-list-tile-title>
            </v-list-tile-content>
          </v-list-tile>

          <v-list-tile>
            <v-list-tile-content @click="spotAdd">
              <v-list-tile-title>전시대 추가</v-list-tile-title>
            </v-list-tile-content>
          </v-list-tile>
          <v-list-tile>
            <v-list-tile-content @click="spotEdit">
              <v-list-tile-title>전시대 편집</v-list-tile-title>
            </v-list-tile-content>
          </v-list-tile>
        </v-list-group>
      </v-list>
    </v-flex>
  </v-layout>
</template>

<script>
  export default {
    data() {
      return {}
    },
    methods: {
      userAdd: function () {
        this.$router.push('/home/userAdd')
      },
      spotAdd: function () {
        this.$router.push('/home/spotAdd')
      },
      userEdit: function () {
        this.$router.push('/home/userEdit')
      },
      spotEdit: function () {
        this.$router.push('/home/spotEdit')
      }
    }
  }
</script>

<style scoped>

</style>

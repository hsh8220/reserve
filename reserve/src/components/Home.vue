<template>
  <v-app id="reserve">
    <v-navigation-drawer
      v-model="drawer"
      fixed
      app
    >
      <v-list dense>
        <v-list-tile @click="">
          <v-list-tile-action>
            <v-icon>home</v-icon>
          </v-list-tile-action>
          <v-list-tile-content>
            <v-list-tile-title @click="home()">처음으로</v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>
        <v-list-tile @click="">
          <v-list-tile-action>
            <v-icon>power_settings_new</v-icon>
          </v-list-tile-action>
          <v-list-tile-content>
            <v-list-tile-title @click="logout()">로그아웃</v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>
      </v-list>
    </v-navigation-drawer>
    <v-toolbar color="primary" dark fixed app>
      <v-toolbar-side-icon @click.stop="drawer = !drawer"></v-toolbar-side-icon>
      <v-toolbar-title></v-toolbar-title>
    </v-toolbar>
    <router-view/>
  </v-app>
</template>

<script>
  export default {
    name: 'Home',
    data: () => ({
      drawer: false
    }),
    methods: {
      home: function () {
        this.$router.push('/home/spotList')
      },
      logout: function () {
        this.$store.dispatch('LOGOUT')
          .then(() => this.$router.push('/login'))
          .catch(({message}) => this.msg = message)
      }
    }
  }
</script>

<style>
  #reserve {
    margin-top: 60px;
  }
</style>

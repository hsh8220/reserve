<template>
  <v-app id="reserve">
    <v-navigation-drawer
      v-model="drawer"
      fixed
      app
    >
      <v-list dense>
        <v-subheader>메뉴</v-subheader>
        <v-list-tile @click="home">
          <v-list-tile-action>
            <v-icon>home</v-icon>
          </v-list-tile-action>
          <v-list-tile-content>
            <v-list-tile-title>목록 보기</v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>
        <v-list-tile v-if="setAuth" @click="setting">
          <v-list-tile-action>
            <v-icon>settings</v-icon>
          </v-list-tile-action>
          <v-list-tile-content>
            <v-list-tile-title>관리자 설정</v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>
        <v-divider></v-divider>
        <v-subheader>로그 아웃</v-subheader>
        <v-list-tile @click="logout">
          <v-list-tile-action>
            <v-icon>power_settings_new</v-icon>
          </v-list-tile-action>
          <v-list-tile-content>
            <v-list-tile-title>로그아웃</v-list-tile-title>
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
    data() {
      return {
        drawer: false,
        setAuth: sessionStorage.role == 'ADMIN'
      }
    },
    methods: {
      home: function () {
        this.$router.push('/home/spotList')
        this.drawer = false
      },
      logout: function () {
        this.$store.dispatch('LOGOUT')
          .then(() => this.$router.push('/login'))
          .catch(({message}) => this.msg = message)
      },
      setting: function () {
        this.$router.push('/home/setting')
        this.drawer = false
      }
    }
  }
</script>

<style>
  #reserve {
    margin-top: 60px;
  }
</style>

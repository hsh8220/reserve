<template>
  <v-dialog
    v-model="dialog"
    persistent
    width="200"
  >
    <v-card
      color="primary"
      dark
    >
      <v-card-text>
        잠시만 기다려 주세요...
        <v-progress-linear
          indeterminate
          color="white"
          class="mb-0"
        ></v-progress-linear>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script>
  export default {
    name: "Loader",
    props: {
      dialog: Boolean
    }
  }
</script>

<style scoped>

</style>

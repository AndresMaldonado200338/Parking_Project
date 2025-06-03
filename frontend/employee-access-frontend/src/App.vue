<template>
  <div id="app">
    <router-view />
    <Toast />
  </div>
</template>

<script>
import { Toast } from 'vue-toastification'
import 'vue-toastification/dist/index.css'

export default {
  components: {
    Toast
  }
}
</script>

<style>
@import './assets/styles/main.css';
</style>
<script setup lang="ts">
import Navbar from "@/components/Navbar.vue";
import RequestForm from "@/components/RequestForm.vue";
import InfoCard from "@/components/InfoCard.vue";
</script>

<template>
  <Navbar></Navbar>
  <div class="app">
    <RequestForm></RequestForm>
    <InfoCard></InfoCard>
  </div>
</template>

<style scoped>
.app {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
</style>

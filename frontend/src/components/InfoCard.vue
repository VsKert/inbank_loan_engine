<script setup lang="ts">
import {ref} from "vue";
import {healthCheck} from "@/api/LoanAPI.ts";
import type {AxiosError} from "axios";

const health = ref("");

async function serverHealthCheck() {
  health.value = "";
  try {
    health.value = await healthCheck();
    console.log(health.value);
  } catch (e) {
    const err = e as AxiosError;
    console.log(err);
  }
}

</script>

<template>
<div class="content">
  <h2>Supported user IDs:</h2>
  <p>49002010965 - DEBT</p>
  <p>49002010976 - Segment 1 (100)</p>
  <p>49002010987 - Segment 2 (300)</p>
  <p>49002010998 - Segment 3 (1000)</p>
  <button @click="serverHealthCheck">Health check</button>
  <p v-if="health"> {{ health }}</p>

</div>
</template>

<style scoped>

</style>
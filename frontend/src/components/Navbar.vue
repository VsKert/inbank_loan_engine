<script setup lang="ts">
import inbank_logo from '@/assets/inbank.png';

function navigateToLinkedIn() {
  window.open('https://www.linkedin.com/in/kert-k-veskus/', '_blank')
}
</script>

<template>
  <div class="content">
    <div class="left">
      <img :src="inbank_logo" alt="inbank-logo" class="logo">
    </div>
    <div class="right">
      <button @click="navigateToLinkedIn">My LinkedIn</button>
    </div>
  </div>

</template>

<style scoped>
.content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-direction: row;
  min-height: 10vh;
  width: 100%;
  background-color: var(--color-background);
  box-shadow: 1px 1px 1px grey;
}

.right {
  padding-right: 1rem;
}

.logo {
  height:6rem;
  width: auto;
}

</style>
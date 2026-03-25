<script setup lang="ts">

import {ref} from "vue";
import {type LoanRequest, requestLoan} from "@/api/LoanAPI.ts";
import type {AxiosError} from "axios";

const form = {
  userId: ref(0),
  loanAmount: ref(0),
  loanPeriod: ref(0)
}

const errors = ref<string[]>([]);

async function submitLoanRequest() {
  errors.value = []
  const formData: LoanRequest = {
    userId: form.userId.value,
    loanPeriod: form.loanPeriod.value,
    amount: form.loanAmount.value
  }

  try {
    const res = await requestLoan(formData);
    console.log(res)
  } catch (e) {
    const err = e as AxiosError
    console.log(err.message)
    errors.value.push(err.message)
  }

}

</script>

<template>
  <div class="content">
    <form class="loan">
      <h1>Submit loan application</h1>
      <div class="form-group">
        <label for="user-id">User ID</label>
        <input id="user-id" v-model.number="form.userId.value">
      </div>

      <div class="form-group">
        <label for="loan-amount">Loan amount</label>
        <input id="loan-amount" v-model.number="form.loanAmount.value">
      </div>

      <div class="form-group">
        <label for="loan-period">Loan period</label>
        <input id="loan-period" v-model.number="form.loanPeriod.value">
      </div>
    </form>

    <div class="form-actions">
      <button type="submit" @click="submitLoanRequest">Submit</button>
    </div>

    <div class="error-container">
      <p class="error-message" v-for="(error, index) in errors" :key="index"> {{ error }}</p>
    </div>
  </div>

</template>

<style scoped>
.loan {
  display: flex;
  flex-direction: column;
}

.form-group {
  display: flex;
  flex-direction: column;
}

.error-message {
  color: red;
}

</style>
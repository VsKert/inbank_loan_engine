<script setup lang="ts">
import Navbar from "@/components/Navbar.vue";
import {reactive, ref} from "vue";
import {healthCheck, type LoanRequest, type LoanResponse, requestLoan} from "@/api/LoanAPI.ts";
import type {AxiosError} from "axios";

const health = ref("");
const errors = ref<string[]>([]);

const form = reactive({
  userId: 0,
  loanAmount: 0,
  loanPeriod: 0
});

const response = ref<LoanResponse | null>(null);

async function serverHealthCheck() {
  health.value = "";
  try {
    health.value = await healthCheck();
    console.log(health.value);
  } catch (e) {
    const err = e as AxiosError;
    errors.value.push(err.message)
    console.log(err);
  }
}

function changeId(id: number) {
  form.userId = id;
}

async function submitLoanRequest() {
  response.value = null;
  errors.value = []
  const formData: LoanRequest = {
    userId: form.userId,
    loanPeriod: form.loanPeriod,
    amount: form.loanAmount
  }

  try {
    const res = await requestLoan(formData);
    console.log(res)
    response.value = res;
  } catch (e) {
    const err = e as AxiosError<Record<string, string>>
    if (err.response?.data) {
      // Validation errors from @Valid — { field: message, ... }
      errors.value = Object.values(err.response.data)
    } else {
      errors.value.push(err.message)
    }
    console.log(err.message)
  }

}

</script>

<template>
  <Navbar></Navbar>
  <div class="app">
    <div class="request">
      <form class="loan">
        <h1>Submit loan application</h1>
        <div class="form-group">
          <label for="user-id">User ID</label>
          <input id="user-id" v-model.number="form.userId">
        </div>

        <div class="form-group">
          <label for="loan-amount">Loan amount (€)</label>
          <input id="loan-amount" v-model.number="form.loanAmount">
        </div>

        <div class="form-group">
          <label for="loan-period">Loan period (months)</label>
          <input id="loan-period" v-model.number="form.loanPeriod">
        </div>
      </form>

      <div class="form-actions">
        <button type="submit" @click="submitLoanRequest">Submit</button>
      </div>

      <div v-if="response" class="response" :class="response!.approved ? 'approved' : 'rejected'">
        <p>Maximum allowed amount: {{response!.amount}}€</p>
        <p>Approved: {{response!.approved}} </p>
        <p v-if="response!.updatedMinimumLoanPeriod != null">
          Minimum loan period: {{response!.updatedMinimumLoanPeriod}} months
        </p>
      </div>

      <div :class="['response', 'rejected']" v-if="errors.length > 0">
        <p v-for="(error, index) in errors" :key="index"> {{ error }}</p>
      </div>
    </div>

    <div class="info">
      <h1>Supported user IDs: (clickable)</h1>
      <ul>
        <li @click="changeId(49002010965)">49002010965 - DEBT</li>
        <li @click="changeId(49002010976)">49002010976 - Segment 1 (100)</li>
        <li @click="changeId(49002010987)">49002010987 - Segment 2 (300)</li>
        <li @click="changeId(49002010998)">49002010998 - Segment 3 (1000)</li>
      </ul>

      <div class="health">
        <button @click="serverHealthCheck">Health check</button>
        <p v-if="health"> {{ health }}</p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.app {
  display: flex;
  flex-direction: row;
  justify-content: center;
  gap: 16rem;
}

.request {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.loan {
  display: flex;
  flex-direction: column;
}

.form-group {
  display: flex;
  flex-direction: column;
  margin: 1rem;
}

.error-message {
  color: red;
}

.health {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  gap: 1rem;
}

.info li {
  color: rgb(33 9 58);
  padding: 0.5rem;
}

.info li:hover {
  cursor: pointer;
  text-decoration: underline;
}

.response {
  padding: .75rem 1.875rem;
  margin: 1rem;
  border-radius: 20px;
}

.rejected {
  background-color: #f14d4d;
}

.approved {
  background-color: #81da81;
}

</style>

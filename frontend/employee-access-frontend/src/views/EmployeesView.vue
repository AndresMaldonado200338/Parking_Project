<template>
  <div class="employee-view">
    <div class="header">
      <h1>Employee Management</h1>
      <router-link to="/employees/new" class="btn btn-primary">
        Add New Employee
      </router-link>
    </div>

    <div class="employee-list">
      <EmployeeList />
    </div>
  </div>
</template>

<script setup>
import EmployeeList from '@/components/employees/EmployeeList.vue'
</script>

<style scoped lang="scss">
.employee-view {
  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 2rem;
  }
}
</style>
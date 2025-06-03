<template>
  <div class="access-view">
    <NavBar />
    <div class="main-content">
      <SideMenu />
      <div class="content">
        <h1>Control de Acceso</h1>
        <div class="access-content">
          <AccessControl />
          <EmployeeReport />
          <FullReport />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import NavBar from '@/components/shared/NavBar.vue'
import SideMenu from '@/components/shared/SideMenu.vue'
import AccessControl from '@/components/access/AccessControl.vue'
import EmployeeReport from '@/components/access/EmployeeReport.vue'
import FullReport from '@/components/access/FullReport.vue'

export default {
  components: {
    NavBar,
    SideMenu,
    AccessControl,
    EmployeeReport,
    FullReport
  }
}
</script>

<style scoped>
.access-view {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.main-content {
  display: flex;
  flex: 1;
}

.content {
  flex: 1;
  padding: 2rem;
  background-color: #f5f7fa;
}

.access-content {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  margin-top: 1.5rem;
}
</style>
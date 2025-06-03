<template>
  <div class="home-view">
    <NavBar />
    <div class="main-content">
      <SideMenu />
      <div class="content">
        <h1>Bienvenido al Sistema</h1>
        <div class="dashboard">
          <div class="dashboard-card">
            <h3>Total Empleados</h3>
            <p>{{ employeeCount }}</p>
          </div>
          <div class="dashboard-card">
            <h3>Registros Hoy</h3>
            <p>{{ todayRecords }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import NavBar from '../components/shared/NavBar.vue'
import SideMenu from '../components/shared/SideMenu.vue'
import { employeeApi, accessApi } from '../services/api'
import { toast } from 'vue-toastification'

export default {
  components: {
    NavBar,
    SideMenu
  },
  setup() {
    const employeeCount = ref(0)
    const todayRecords = ref(0)

    const fetchData = async () => {
      try {
        // Obtener total de empleados
        const employeesResponse = await employeeApi.get('/findallemployees')
        employeeCount.value = employeesResponse.data.length

        // Obtener registros de hoy
        const today = new Date().toISOString().split('T')[0]
        const accessResponse = await accessApi.get(`/allemployeesbydate?date=${today}`)
        todayRecords.value = accessResponse.data.length
      } catch (error) {
        toast.error('Error al cargar datos del dashboard')
        console.error('Error:', error)
      }
    }

    onMounted(() => {
      fetchData()
    })

    return { employeeCount, todayRecords }
  }
}
</script>

<style scoped>
.home-view {
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

.dashboard {
  display: flex;
  gap: 1.5rem;
  margin-top: 2rem;
}

.dashboard-card {
  flex: 1;
  padding: 1.5rem;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  text-align: center;
}

.dashboard-card h3 {
  color: #7f8c8d;
  margin-bottom: 0.5rem;
}

.dashboard-card p {
  font-size: 2rem;
  font-weight: bold;
  color: #2c3e50;
  margin: 0;
}
</style>
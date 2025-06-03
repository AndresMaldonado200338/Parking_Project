<template>
  <div class="access-control">
    <h2>Registro de Accesos</h2>
    <div class="access-actions">
      <div class="employee-selector">
        <label for="employeeId">Empleado:</label>
        <select id="employeeId" v-model="selectedEmployee">
          <option v-for="employee in employees" :key="employee.document" :value="employee.document">
            {{ employee.firstname }} {{ employee.lastname }} ({{ employee.document }})
          </option>
        </select>
      </div>
      <div class="access-buttons">
        <button @click="registerCheckIn" class="btn-checkin">Registrar Entrada</button>
        <button @click="registerCheckOut" class="btn-checkout">Registrar Salida</button>
      </div>
    </div>
    <div v-if="lastAccess" class="last-access">
      <h3>Último registro:</h3>
      <p><strong>Empleado:</strong> {{ lastAccess.employeeId }}</p>
      <p><strong>Tipo:</strong> {{ lastAccess.eventType === 'ENTRADA' ? 'Entrada' : 'Salida' }}</p>
      <p><strong>Fecha/Hora:</strong> {{ formatDateTime(lastAccess.accessDateTime) }}</p>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { employeeApi, accessApi } from '../../services/api'
import { toast } from 'vue-toastification'

export default {
  setup() {
    const employees = ref([])
    const selectedEmployee = ref('')
    const lastAccess = ref(null)

    const fetchEmployees = async () => {
      try {
        const response = await employeeApi.get('/findallemployees')
        employees.value = response.data.filter(e => e.status)
        if (employees.value.length > 0) {
          selectedEmployee.value = employees.value[0].document
        }
      } catch (error) {
        toast.error('Error al cargar empleados')
        console.error('Error:', error)
      }
    }

    const registerCheckIn = async () => {
      if (!selectedEmployee.value) {
        toast.warning('Seleccione un empleado')
        return
      }
      try {
        const response = await accessApi.post('/usercheckin', {
          employeeId: selectedEmployee.value
        })
        lastAccess.value = response.data
        toast.success('Entrada registrada correctamente')
      } catch (error) {
        toast.error('Error al registrar entrada')
        console.error('Error:', error)
      }
    }

    const registerCheckOut = async () => {
      if (!selectedEmployee.value) {
        toast.warning('Seleccione un empleado')
        return
      }
      try {
        const response = await accessApi.post('/usercheckout', {
          employeeId: selectedEmployee.value
        })
        lastAccess.value = response.data
        toast.success('Salida registrada correctamente')
      } catch (error) {
        toast.error('Error al registrar salida')
        console.error('Error:', error)
      }
    }

    const formatDateTime = (dateTime) => {
      return new Date(dateTime).toLocaleString()
    }

    onMounted(() => {
      fetchEmployees()
    })

    return {
      employees,
      selectedEmployee,
      lastAccess,
      registerCheckIn,
      registerCheckOut,
      formatDateTime
    }
  }
}
</script>

<style scoped>
.access-control {
  background: white;
  padding: 1.5rem;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

h2 {
  margin-bottom: 1.5rem;
  color: #2c3e50;
}

.access-actions {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin-bottom: 1.5rem;
}

.employee-selector {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.employee-selector label {
  font-weight: 500;
  color: #34495e;
}

.employee-selector select {
  flex: 1;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.access-buttons {
  display: flex;
  gap: 1rem;
}

.btn-checkin, .btn-checkout {
  flex: 1;
  padding: 0.75rem;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s;
}

.btn-checkin {
  background-color: #2ecc71;
  color: white;
}

.btn-checkin:hover {
  background-color: #27ae60;
}

.btn-checkout {
  background-color: #e74c3c;
  color: white;
}

.btn-checkout:hover {
  background-color: #c0392b;
}

.last-access {
  margin-top: 1.5rem;
  padding: 1rem;
  background-color: #f8f9fa;
  border-radius: 4px;
}

.last-access h3 {
  margin-bottom: 0.5rem;
  color: #2c3e50;
}

.last-access p {
  margin: 0.25rem 0;
  color: #34495e;
}
</style>
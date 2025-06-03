<template>
  <div class="employee-report">
    <h3>Reporte por Empleado</h3>
    <form @submit.prevent="generateReport">
      <div class="form-group">
        <label for="employeeId">Empleado:</label>
        <select id="employeeId" v-model="selectedEmployee">
          <option v-for="employee in employees" :key="employee.document" :value="employee.document">
            {{ employee.firstname }} {{ employee.lastname }} ({{ employee.document }})
          </option>
        </select>
      </div>
      <div class="form-row">
        <div class="form-group">
          <label for="startDate">Fecha Inicio:</label>
          <input type="date" id="startDate" v-model="startDate" required />
        </div>
        <div class="form-group">
          <label for="endDate">Fecha Fin:</label>
          <input type="date" id="endDate" v-model="endDate" required />
        </div>
      </div>
      <button type="submit" class="btn-generate">Generar Reporte</button>
    </form>

    <div v-if="reportData" class="report-results">
      <h4>Resultados:</h4>
      <div v-for="(record, index) in reportData.accessRecords" :key="index" class="record">
        <p><strong>Fecha:</strong> {{ formatDate(record.entryTime) }}</p>
        <p><strong>Entrada:</strong> {{ formatTime(record.entryTime) }}</p>
        <p><strong>Salida:</strong> {{ formatTime(record.exitTime) }}</p>
        <p><strong>Duración:</strong> {{ record.duration }}</p>
        <hr v-if="index < reportData.accessRecords.length - 1" />
      </div>
      <div class="total">
        <p><strong>Duración Total:</strong> {{ reportData.totalDuration }}</p>
      </div>
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
    const startDate = ref('')
    const endDate = ref('')
    const reportData = ref(null)

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

    const generateReport = async () => {
      if (!selectedEmployee.value || !startDate.value || !endDate.value) {
        toast.warning('Complete todos los campos')
        return
      }

      try {
        const response = await accessApi.get('/employeebydates', {
          params: {
            employeeId: selectedEmployee.value,
            startDate: startDate.value,
            endDate: endDate.value
          }
        })
        reportData.value = response.data[0] || null
        toast.success('Reporte generado correctamente')
      } catch (error) {
        toast.error('Error al generar el reporte')
        console.error('Error:', error)
      }
    }

    const formatDate = (dateTime) => {
      return new Date(dateTime).toLocaleDateString()
    }

    const formatTime = (dateTime) => {
      return new Date(dateTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
    }

    onMounted(() => {
      fetchEmployees()
      // Set default dates (today)
      const today = new Date().toISOString().split('T')[0]
      startDate.value = today
      endDate.value = today
    })

    return {
      employees,
      selectedEmployee,
      startDate,
      endDate,
      reportData,
      generateReport,
      formatDate,
      formatTime
    }
  }
}
</script>

<style scoped>
.employee-report {
  background: white;
  padding: 1.5rem;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

h3 {
  margin-bottom: 1.5rem;
  color: #2c3e50;
}

.form-group {
  margin-bottom: 1rem;
}

label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
  color: #34495e;
}

select, input {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.form-row {
  display: flex;
  gap: 1rem;
}

.form-row .form-group {
  flex: 1;
}

.btn-generate {
  margin-top: 1rem;
  padding: 0.75rem;
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s;
}

.btn-generate:hover {
  background-color: #2980b9;
}

.report-results {
  margin-top: 1.5rem;
  padding: 1rem;
  background-color: #f8f9fa;
  border-radius: 4px;
}

.report-results h4 {
  margin-bottom: 1rem;
}

.record {
  margin-bottom: 1rem;
}

.total {
  margin-top: 1rem;
  padding-top: 1rem;
  border-top: 1px solid #ddd;
  font-weight: bold;
}
</style>
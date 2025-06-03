<template>
  <div class="full-report">
    <h3>Reporte General por Fecha</h3>
    <form @submit.prevent="generateReport">
      <div class="form-group">
        <label for="reportDate">Fecha:</label>
        <input type="date" id="reportDate" v-model="reportDate" required />
      </div>
      <button type="submit" class="btn-generate">Generar Reporte</button>
    </form>

    <div v-if="reportData.length > 0" class="report-results">
      <h4>Resultados:</h4>
      <table>
        <thead>
          <tr>
            <th>Empleado</th>
            <th>Entrada</th>
            <th>Salida</th>
            <th>Duración</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(record, index) in reportData" :key="index">
            <td>{{ record.employeeName }}</td>
            <td>{{ formatTime(record.entryTime) }}</td>
            <td>{{ formatTime(record.exitTime) }}</td>
            <td>{{ record.duration }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <div v-else-if="reportSubmitted" class="no-results">
      <p>No hay registros para la fecha seleccionada</p>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'
import { accessApi } from '../../services/api'
import { toast } from 'vue-toastification'

export default {
  setup() {
    const reportDate = ref('')
    const reportData = ref([])
    const reportSubmitted = ref(false)

    const generateReport = async () => {
      if (!reportDate.value) {
        toast.warning('Seleccione una fecha')
        return
      }

      try {
        const response = await accessApi.get('/allemployeesbydate', {
          params: {
            date: reportDate.value
          }
        })
        reportData.value = response.data
        reportSubmitted.value = true
        toast.success('Reporte generado correctamente')
      } catch (error) {
        toast.error('Error al generar el reporte')
        console.error('Error:', error)
      }
    }

    const formatTime = (dateTime) => {
      return new Date(dateTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
    }

    // Set default date (today)
    reportDate.value = new Date().toISOString().split('T')[0]

    return {
      reportDate,
      reportData,
      reportSubmitted,
      generateReport,
      formatTime
    }
  }
}
</script>

<style scoped>
.full-report {
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

input {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
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
}

table {
  width: 100%;
  border-collapse: collapse;
}

th, td {
  padding: 0.75rem;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

th {
  background-color: #f8f9fa;
  font-weight: 500;
}

.no-results {
  margin-top: 1.5rem;
  padding: 1rem;
  background-color: #f8f9fa;
  border-radius: 4px;
  text-align: center;
  color: #7f8c8d;
}
</style>
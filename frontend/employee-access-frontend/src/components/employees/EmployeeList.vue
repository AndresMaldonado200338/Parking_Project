<template>
  <div class="employee-list-container">
    <div class="filters">
      <input type="text" v-model="searchTerm" placeholder="Buscar empleado..." />
      <select v-model="statusFilter">
        <option value="all">Todos</option>
        <option value="active">Activos</option>
        <option value="inactive">Inactivos</option>
      </select>
    </div>

    <table v-if="filteredEmployees.length > 0">
      <thead>
        <tr>
          <th>Nombre</th>
          <th>Apellido</th>
          <th>Email</th>
          <th>Teléfono</th>
          <th>Rol</th>
          <th>Estado</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="employee in filteredEmployees" :key="employee.id">
          <td>{{ employee.firstname }}</td>
          <td>{{ employee.lastname }}</td>
          <td>{{ employee.email }}</td>
          <td>{{ employee.phone }}</td>
          <td>{{ employee.role }}</td>
          <td>
            <span :class="['status-badge', employee.status ? 'status-active' : 'status-inactive']">
              {{ employee.status ? 'Activo' : 'Inactivo' }}
            </span>
          </td>
          <td>
            <button @click="$emit('edit-employee', employee)">Editar</button>
            <button @click="toggleStatus(employee.id, !employee.status)">
              {{ employee.status ? 'Desactivar' : 'Activar' }}
            </button>
            <button @click="deleteEmployee(employee.id)" class="delete-button">
              Eliminar
            </button>
          </td>
        </tr>
      </tbody>
    </table>
    <div v-else>
      <p>No se encontraron empleados que coincidan con los criterios de búsqueda.</p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useEmployeeStore } from '../../services/storage' //
import { toast } from 'vue-toastification' //

const employeeStore = useEmployeeStore() //
const searchTerm = ref('') //
const statusFilter = ref('all') //

// Usar computed para filteredEmployees
const filteredEmployees = computed(() => { //
  return employeeStore.employees.filter(employee => { //
    const matchesSearch = //
      employee.firstname.toLowerCase().includes(searchTerm.value.toLowerCase()) || //
      employee.lastname.toLowerCase().includes(searchTerm.value.toLowerCase()) || //
      employee.email.toLowerCase().includes(searchTerm.value.toLowerCase()) || //
      employee.phone.toLowerCase().includes(searchTerm.value.toLowerCase()) //

    const matchesStatus = //
      statusFilter.value === 'all' || //
      (statusFilter.value === 'active' && employee.status) || //
      (statusFilter.value === 'inactive' && !employee.status) //

    return matchesSearch && matchesStatus //
  })
})

onMounted(async () => { //
  await employeeStore.fetchEmployees() //
})

const toggleStatus = async (id, status) => { //
  try { //
    await employeeStore.updateEmployeeStatus(id, status) //
    toast.success(`Empleado ${status ? 'activado' : 'desactivado'} correctamente`) //
  } catch (error) { //
    toast.error('Error al cambiar el estado del empleado') //
    console.error('Error:', error) //
  }
}

const deleteEmployee = async (id) => { //
  if (confirm('¿Está seguro que desea eliminar este empleado?')) { //
    try { //
      await employeeStore.deleteEmployee(id) //
      toast.success('Empleado eliminado correctamente') //
    } catch (error) { //
      toast.error('Error al eliminar el empleado') //
      console.error('Error:', error) //
    }
  }
}
</script>

<style scoped>
/* Estilos básicos para la tabla y filtros (puedes personalizarlos) */
.employee-list-container {
  padding: 20px;
}

.filters {
  margin-bottom: 20px;
  display: flex;
  gap: 10px;
}

.filters input[type="text"],
.filters select {
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

th, td {
  border: 1px solid #ddd;
  padding: 10px;
  text-align: left;
}

th {
  background-color: #f4f4f4;
}

.status-badge {
  padding: 3px 8px;
  border-radius: 12px;
  font-size: 0.9em;
  color: white;
}

.status-active {
  background-color: #4CAF50; /* Verde */
}

.status-inactive {
  background-color: #f44336; /* Rojo */
}

button {
  margin-right: 5px;
  padding: 6px 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  opacity: 0.8;
}

.delete-button {
  background-color: #f44336; /* Rojo */
  color: white;
}
</style>
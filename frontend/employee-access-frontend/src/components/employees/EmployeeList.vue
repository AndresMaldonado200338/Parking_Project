<script setup>
import { ref, computed, onMounted } from 'vue'
import { useEmployeeStore } from '../../services/storage'
import { toast } from 'vue-toastification'

const employeeStore = useEmployeeStore()
const searchTerm = ref('')
const statusFilter = ref('all')

// Usar computed para filteredEmployees
const filteredEmployees = computed(() => {
  return employeeStore.employees.filter(employee => {
    const matchesSearch = 
      employee.firstname.toLowerCase().includes(searchTerm.value.toLowerCase()) ||
      employee.lastname.toLowerCase().includes(searchTerm.value.toLowerCase()) ||
      employee.email.toLowerCase().includes(searchTerm.value.toLowerCase()) ||
      employee.phone.toLowerCase().includes(searchTerm.value.toLowerCase())

    const matchesStatus = 
      statusFilter.value === 'all' ||
      (statusFilter.value === 'active' && employee.status) ||
      (statusFilter.value === 'inactive' && !employee.status)

    return matchesSearch && matchesStatus
  })
})

onMounted(async () => {
  await employeeStore.fetchEmployees()
})

const toggleStatus = async (id, status) => {
  try {
    await employeeStore.updateEmployeeStatus(id, status)
    toast.success(`Empleado ${status ? 'activado' : 'desactivado'} correctamente`)
  } catch (error) {
    toast.error('Error al cambiar el estado del empleado')
    console.error('Error:', error)
  }
}

const deleteEmployee = async (id) => {
  if (confirm('¿Está seguro que desea eliminar este empleado?')) {
    try {
      await employeeStore.deleteEmployee(id)
      toast.success('Empleado eliminado correctamente')
    } catch (error) {
      toast.error('Error al eliminar el empleado')
      console.error('Error:', error)
    }
  }
}
</script>
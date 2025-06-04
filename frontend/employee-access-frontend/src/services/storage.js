import { reactive } from 'vue'

const authStore = reactive({
  token: localStorage.getItem('token') || null,
  userID: localStorage.getItem('userID') || null,
  
  get isAuthenticated() {
    return !!this.token
  },
  
  login(token, userID) {
    this.token = token
    this.userID = userID
    localStorage.setItem('token', token)
    localStorage.setItem('userID', userID)
  },
  
  logout() {
    this.token = null
    this.userID = null
    localStorage.removeItem('token')
    localStorage.removeItem('userID')
  }
})

export function useAuthStore() {
  return authStore
}

const employeeStore = reactive({
  employees: [], // Esto estaba en el EmployeeList.vue antes, ahora lo maneja el store
  async fetchEmployees() {
    // Importa employeeApi aquí dentro de la función para evitar ciclos si api.js importa algo de storage.js
    const { employeeApi } = await import('./api'); 
    try {
      const response = await employeeApi.get('/findallemployees'); // Usar la instancia importada
      this.employees = response.data;
    } catch (error) {
      console.error('Error fetching employees en storage.js:', error);
      this.employees = []; 
      // Considera relanzar el error o manejarlo de forma que el componente sepa que falló
      // throw error; 
    }
  },
  async updateEmployeeStatus(employeeId, status) {
    const { employeeApi } = await import('./api');
    try {
      await employeeApi.patch(`/disableemployee/${employeeId}`, { status });
      const index = this.employees.findIndex(e => e.document === employeeId);
      if (index !== -1) {
        this.employees[index].status = status;
      }
    } catch (error) {
      console.error('Error updating employee status en storage.js:', error);
      throw error; 
    }
  },
  async deleteEmployee(employeeId) {
    // No necesitamos employeeApi aquí si solo llama a updateEmployeeStatus
    try {
      console.warn(`deleteEmployee con ID ${employeeId} en storage.js no está implementado con un endpoint DELETE. Usando lógica de desactivación.`);
      await this.updateEmployeeStatus(employeeId, false); 
    } catch (error) {
      console.error('Error deleting employee en storage.js:', error);
      throw error;
    }
  }
  // Puedes añadir más acciones como createEmployee, updateEmployee si es necesario
});

export function useEmployeeStore() { // Asegúrate de que esto se exporte
  return employeeStore;
}
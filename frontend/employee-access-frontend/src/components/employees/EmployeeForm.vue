<template>
  <div class="employee-form">
    <h2>{{ isEditing ? 'Editar Empleado' : 'Crear Empleado' }}</h2>
    <form @submit.prevent="handleSubmit">
      <div class="form-group">
        <label for="document">Documento</label>
        <input
          type="text"
          id="document"
          v-model="formData.document"
          :disabled="isEditing"
          required
        />
      </div>
      <div class="form-group">
        <label for="firstname">Nombres</label>
        <input
          type="text"
          id="firstname"
          v-model="formData.firstname"
          required
        />
      </div>
      <div class="form-group">
        <label for="lastname">Apellidos</label>
        <input
          type="text"
          id="lastname"
          v-model="formData.lastname"
          required
        />
      </div>
      <div class="form-group">
        <label for="email">Email</label>
        <input
          type="email"
          id="email"
          v-model="formData.email"
          required
        />
      </div>
      <div class="form-group">
        <label for="phone">Teléfono</label>
        <input
          type="text"
          id="phone"
          v-model="formData.phone"
          required
        />
      </div>
      <div class="form-actions">
        <button type="submit" class="btn-submit">
          {{ isEditing ? 'Actualizar' : 'Guardar' }}
        </button>
        <button type="button" class="btn-cancel" @click="resetForm">
          Cancelar
        </button>
      </div>
    </form>
  </div>
</template>

<script>
import { ref, watch } from 'vue'
import { employeeApi } from '../../services/api'
import { toast } from 'vue-toastification'

export default {
  props: {
    employee: {
      type: Object,
      default: null
    }
  },
  setup(props) {
    const formData = ref({
      document: '',
      firstname: '',
      lastname: '',
      email: '',
      phone: ''
    })

    const isEditing = ref(false)

    watch(() => props.employee, (newVal) => {
      if (newVal) {
        isEditing.value = true
        formData.value = { ...newVal }
      } else {
        resetForm()
      }
    }, { immediate: true })

    const resetForm = () => {
      formData.value = {
        document: '',
        firstname: '',
        lastname: '',
        email: '',
        phone: ''
      }
      isEditing.value = false
    }

    const handleSubmit = async () => {
      try {
        if (isEditing.value) {
          await employeeApi.put(`/updateemployee/${formData.value.document}`, formData.value)
          toast.success('Empleado actualizado correctamente')
        } else {
          await employeeApi.post('/createemployee', formData.value)
          toast.success('Empleado creado correctamente')
        }
        resetForm()
        emit('refresh')
      } catch (error) {
        toast.error('Error al guardar el empleado')
        console.error('Error:', error)
      }
    }

    return { formData, isEditing, handleSubmit, resetForm }
  }
}
</script>

<style scoped>
.employee-form {
  background: white;
  padding: 1.5rem;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

h2 {
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

.form-actions {
  display: flex;
  gap: 1rem;
  margin-top: 1.5rem;
}

.btn-submit {
  flex: 1;
  padding: 0.75rem;
  background-color: #2ecc71;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s;
}

.btn-submit:hover {
  background-color: #27ae60;
}

.btn-cancel {
  flex: 1;
  padding: 0.75rem;
  background-color: #e74c3c;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s;
}

.btn-cancel:hover {
  background-color: #c0392b;
}
</style>
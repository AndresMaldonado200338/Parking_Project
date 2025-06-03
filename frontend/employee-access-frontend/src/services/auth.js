import { loginApi } from './api'
import { toast } from 'vue-toastification'

export const authService = {
  async login(userID, password) {
    try {
      const response = await loginApi.post('/authuser', {
        userID: Number(userID),
        password
      })
      
      if (response.data.token) {
        return response.data
      } else {
        toast.error('Credenciales inválidas')
        return null
      }
    } catch (error) {
      toast.error('Error al autenticar')
      console.error('Auth error:', error)
      return null
    }
  }
}
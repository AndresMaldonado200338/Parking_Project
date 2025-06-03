import axios from 'axios'

const employeeApi = axios.create({
  baseURL: 'http://localhost:8080/employee',
  headers: {
    'Content-Type': 'application/json'
  }
})

const accessApi = axios.create({
  baseURL: 'http://localhost:8080/access',
  headers: {
    'Content-Type': 'application/json'
  }
})

const loginApi = axios.create({
  baseURL: 'http://localhost:8081/login',
  headers: {
    'Content-Type': 'application/json'
  }
})

export { employeeApi, accessApi, loginApi }
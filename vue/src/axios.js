import axios from 'axios'
import Element from 'element-ui'
import router from './router'
import store from './store'

//网址base标签
axios.defaults.baseURL = "http://localhost:8081"

// 前置拦截
axios.interceptors.request.use(config => {
  return config
})


//返回结果之前的一个拦截
axios.interceptors.response.use(response => {

    if (response.status === 200) {
      return response
    } else {

      //用elementUI来提示错误信息
      Element.Message.error('错了哦，这是一条错误消息', {duration: 3 * 1000})

      return Promise.reject(response.data.msg)
    }
  },

  error => {
    console.log(error)
    if(error.response.data) {
      error.message = error.response.data.msg
    }

    if(error.response.status === 401) {
      store.commit("REMOVE_INFO")
      router.push("/login")
    }

    Element.Message.error(error.message, {duration: 3 * 1000})

    return Promise.reject(error)
  }


)

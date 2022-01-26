import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  //属性
  state: {
    token: '',
    //取出json字符串，变成json对象
    userInfo: JSON.parse(sessionStorage.getItem("userInfo"))
  },

  //set
  mutations: {
    //state：属性，token：传过来的值
    SET_TOKEN: (state, token) => {
      //给state中的token赋值
      state.token = token
      //存入到localStorage
      localStorage.setItem("token", token)
    },
    SET_USERINFO: (state, userInfo) => {
      state.userInfo = userInfo
      sessionStorage.setItem("userInfo", JSON.stringify(userInfo))
    },
    REMOVE_INFO: (state) => {
      state.token = ''
      state.userInfo = {}
      localStorage.setItem("token", '')
      sessionStorage.setItem("userInfo", JSON.stringify(''))
    }

  },
  // get
  getters: {
    getUser: state => {
      return state.userInfo
    }

  },
  actions: {
  },
  modules: {
  }
})

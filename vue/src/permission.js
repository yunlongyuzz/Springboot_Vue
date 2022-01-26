import router from "./router";

// 路由判断登录 根据路由配置文件的参数
router.beforeEach((to, from, next) => {

  if (to.matched.some(record => record.meta.requireAuth)) { // 判断该路由是否需要登录权限

    const token = localStorage.getItem("token")

    //如果需要权限，看看有没有token
    if (token) {
      //有token
        next()
    }
  //如果没有token，去登录页面
  else {
      next({
        path: '/login'
      })
    }
  }
  //没有requireAuth，直接走
  else {
    next()
  }
})

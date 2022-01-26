<template>
  <div class="mcontaner">

    <!--导入了这个组件，所以可以直接使用这个组件 -->
    <Header></Header>

    <!--分页内容以及博客显示内容 -->
    <div class="block">

      <el-timeline>
        <!-- 循环遍历博客内容 -->
        <el-timeline-item :timestamp="blog.created" placement="top" v-for="blog in blogs">

          <el-card>

            <h4>
              <router-link :to="{name: 'BlogDetail', params: {blogId: blog.id}}">
                {{blog.title}}
              </router-link>
            </h4>
            <p>{{blog.description}}</p>

          </el-card>

        </el-timeline-item>
      </el-timeline>

      <!--显示分页数据 -->
      <el-pagination class="mpage"
                     background
                     layout="prev, pager, next"
                     :current-page="currentPage"
                     :page-size="pageSize"
                     :total="total"
                     @current-change=page>
      </el-pagination>

    </div>

  </div>
</template>

<script>

  //导入这个头部分
  import Header from "../components/Header";


  export default {
    name: "Blogs.vue",
    components: {Header},
    data() {
      return {
        blogs: {},
        currentPage: 1,
        total: 0,
        pageSize: 5
      }
    },

    methods: {

    page(currentPage) {
        const _this = this
        _this.$axios.get("/blogs?currentPage=" + currentPage).then(res => {
          //返回结果
          console.log(res)
          _this.blogs = res.data.data.records
          _this.currentPage = res.data.data.current
          _this.total = res.data.data.total
          _this.pageSize = res.data.data.size

        })
      },


    },

    created() {
      this.page(1)

    }

  }
</script>

<style scoped>

  .mpage {
    margin: 0 auto;
    text-align: center;
  }

</style>

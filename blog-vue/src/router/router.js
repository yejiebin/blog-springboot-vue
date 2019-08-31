import Vue from 'vue'
import Router from 'vue-router'
import index from '@/views/index'
import notfound from '@/views/notfound'
import message from '@/views/message'
import announcement from '@/views/announcement'
import newBlog from '@/views/blog/newBlog'
import myBlog from '@/views/blog/myBlog'
import account from '@/views/user/account'
import editBlog from '@/views/blog/editBlog'
import blog from '@/views/blog/blog'
import forgetPwd from '@/views/user/forgetPwd'
import admins from '@/views/admins/admins'
import userManage from '@/views/admins/userManage'
import codeManage from '@/views/admins/codeManage'
import announcementManage from '@/views/admins/announcementManage'
import blogManage from '@/views/admins/blogManage'
import searchBlog from '@/views/searchBlog'


Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'index',
      component: index
    },
    {
      path: '/message',
      name: 'message',
      component: message
    },
    {
      path: '/announcement',
      name: 'announcement',
      component: announcement
    },
    {
      path: '/newBlog',
      name: 'newBlog',
      component: newBlog
    },
    {
      path: '/myBlog',
      name: 'myBlog',
      component: myBlog
    },
    {
      path: '/blog/:blogId',
      name: 'blog',
      component: blog
    },
    {
      path: '/editBlog/:blogId',
      name: 'editBlog',
      component: editBlog
    },
    {
      path: '/account',
      name: 'account',
      component: account
    },
    {
      path: '/forgetPwd',
      name: 'forgetPwd',
      component: forgetPwd
    },
    {
      path: '/searchBlog/:searchTxt',
      name: 'searchBlog',
      component: searchBlog
    },
    {
      path: '/admins',
      name: 'admins',
      component: admins,
      children: [ //这里就是二级路由的配置
        {
          path: 'userManage',
          name: 'userManage',
          component: userManage
        },
        {
          path: 'codeManage',
          name: 'codeManage',
          component: codeManage
        },
        {
          path: 'announcementManage',
          name: 'announcementManage',
          component: announcementManage
        },
        {
          path: 'blogManage',
          name: 'blogManage',
          component: blogManage
        }
      ]
    },


    {
      path: '*',
      name: 'notfound',
      component: notfound
    }
  ]
})

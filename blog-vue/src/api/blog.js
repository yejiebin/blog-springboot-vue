import request from '@/utils/request'
import qs from 'qs'

export default {
  getBlogHome(page, showCount) {
    return request({
      url: '/blog/home/' + page + '/' + showCount,
      method: 'get'
    })
  },
  getMyBlog(page, showCount) {
    return request({
      url: '/blog/myblog/' + page + '/'+ showCount,
      method: 'get'
    })
  },
  getHotBlog() {
    return request({
      url: '/blog/hotBlog',
      method: 'get'
    })
  },
  getStatisticalBlogByMonth() {
    return request({
      url: '/blog/statisticalBlogByMonth',
      method: 'get'
    })
  },
  sendBlog(blogTitle, blogBody, tagId) {  //发布博客
    return request({
      url: '/blog',
      method: 'post',
      data: qs.stringify({'blogTitle': blogTitle, 'blogBody': blogBody, 'tagId': tagId})
    })
  },
  getBlogById(id) {
    return request({
      url: '/blog/' + id,
      method: 'get'
    })
  },
  editBlog(blogId, blogTitle, blogBody, tagId) {  //修改博客
    return request({
      url: '/blog/' + blogId,
      method: 'put',
      data: qs.stringify({'blogTitle': blogTitle, 'blogBody': blogBody, 'tagId': tagId})
    })
  },
  uploadImg(formdata) {
    return request({
      url: '/file/upload',
      method: 'post',
      data: formdata,
      headers: {'Content-Type': 'multipart/form-data'}
    })
  },
  adminSearchBlog(searchTxt, page, showCount) {
    return request({
      url: '/blog/searchAllBlog/' + page + '/' + showCount + '?searchText=' + searchTxt,
      method: 'get'
    })
  },
  adminGetBlog(page, showCount) {
    return request({
      url: '/blog/AllBlog/' + page + '/' + showCount,
      method: 'get'
    })
  },
  userSearchBlog(searchTxt, page, showCount) {
    return request({
      url: '/blog/searchBlog/' + page + '/' + showCount + '?search=' + searchTxt,
      method: 'get'
    })
  },
  adminDeleteBlog(blogId) {
    return request({
      url: '/blog/admin/' + blogId,
      method: 'delete'
    })
  },
  userDeleteBlog(blogId) {
    return request({
      url: '/blog/' + blogId,
      method: 'delete'
    })
  }
}

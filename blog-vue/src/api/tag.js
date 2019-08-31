import request from '@/utils/request'
import qs from 'qs'

export default {
  getTag() {
    return request({
      url: '/tag',
      method: 'get'
    })
  },
  updateTag(tagId, tagName) {
    return request({
      url: '/tag',
      method: 'put',
      data: qs.stringify({'tagId': tagId, 'tagName': tagName})
    })
  },
  addTag(tagName) {
    return request({
      url: '/tag',
      method: 'post',
      data: qs.stringify({'tagName': tagName})
    })
  },
  deleteTag(id) {
    return request({
      url: '/tag/' + id,
      method: 'delete'
    })
  }
}

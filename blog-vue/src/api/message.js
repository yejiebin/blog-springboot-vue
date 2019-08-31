import request from '@/utils/request'
import qs from 'qs'

export default {
  getMessage(page, showCount) {
    return request({
      url: '/message/' + page + '/' + showCount,
      method: 'get'
    })
  },
  sendMessage(messageBody) {
    return request({
      url: 'message',
      method: 'post',
      data: qs.stringify({'messageBody': messageBody})
    })
  },
  deleteMessage(id) {
    return request({
      url: '/message/' + id,
      method: 'delete'
    })
  }
}

import request from './request'

// 文件上传相关API
export const uploadAPI = {
  // 上传文件（头像、图片等）
  uploadFile: (file: File) => {
    const formData = new FormData()
    formData.append('file', file)
    return request.post<string>('/upload', formData, {
      timeout: 60000
    })
  }
}

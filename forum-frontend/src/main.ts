import { createApp } from 'vue'
import { createPinia } from 'pinia'
import router from './router'
import App from './App.vue'

// 导入Vant组件和样式
import { Button, Tabbar, TabbarItem, NavBar, Icon, Image as VanImage, List, Cell, CellGroup, Form, Field, Toast, Dialog, Empty, Badge, Tabs, Tab, Uploader, ActionSheet, Loading } from 'vant'
import 'vant/lib/index.css'

const app = createApp(App)
const pinia = createPinia()

// 注册Vant组件
app.use(Button)
app.use(Tabbar)
app.use(TabbarItem)
app.use(NavBar)
app.use(Icon)
app.use(VanImage)
app.use(List)
app.use(Cell)
app.use(CellGroup)
app.use(Form)
app.use(Field)
app.use(Toast)
app.use(Dialog)
app.use(Empty)
app.use(Badge)
app.use(Tabs)
app.use(Tab)
app.use(Uploader)
app.use(ActionSheet)
app.use(Loading)

app.use(pinia)
app.use(router)
app.mount('#app')
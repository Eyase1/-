<template>
  <div class="user-management">
    <Navbar/>
    <el-card class="user-card">
      <template #header>
        <div class="card-header">
          <h2>用户管理</h2>
          <el-input
            v-model="searchQuery"
            placeholder="搜索用户..."
            class="search-input"
            clearable
            @clear="handleSearch"
            @input="handleSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
        </div>
      </template>

      <el-table
        :data="filteredUsers"
        style="width: 100%"
        v-loading="loading"
        border
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" width="150" />
        <el-table-column prop="email" label="邮箱" width="200" />
        <el-table-column prop="gender" label="性别" width="180"/>
          <el-table-column prop="role" label="角色" width="180"/>
         
        <!-- <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column> -->
        <el-table-column label="操作" fixed="right" width="200">
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              @click="handleView(scope.row)"
            >
              查看
            </el-button>
            <el-button
              type="warning"
              size="small"
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
            <el-button
              type="danger"
              size="small"
              @click="handleDelete(scope.row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 用户详情对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'view' ? '用户详情' : '编辑用户'"
      width="500px"
    >
      <el-form
        ref="userForm"
        :model="currentUser"
        :rules="rules"
        label-width="100px"
        :disabled="dialogType === 'view'"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="currentUser.username" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="currentUser.email" />
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-input v-model="currentUser.role" />
        </el-form-item>
        <!-- <el-form-item label="状态" prop="status">
          <el-switch
            v-model="currentUser.status"
            :active-value="1"
            :inactive-value="0"
          />
        </el-form-item> -->
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button
            v-if="dialogType === 'edit'"
            type="primary"
            @click="handleSave"
          >
            保存
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import { getAllUsers, updateUser, searchUsers } from '../../api/index'
import Navbar from '@/components/Navbar.vue'

// 数据
const users = ref([])
const loading = ref(false)
const searchQuery = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const dialogVisible = ref(false)
const dialogType = ref('view')
const currentUser = ref({})
const userForm = ref(null)
const allUsers = ref([])

// 表单验证规则
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ]
}

// 过滤后的用户列表
const filteredUsers = computed(() => {
  if (!searchQuery.value) {
    return users.value
  }
  const query = searchQuery.value.toLowerCase()
  return users.value.filter(user => 
    user.username.toLowerCase().includes(query) ||
    user.email.toLowerCase().includes(query)
  )
})

// 获取用户列表
const fetchUsers = async () => {
  loading.value = true
  try {
    const response = await getAllUsers()
    if (response.status === 200) {
      users.value = response.data
      allUsers.value = response.data
      total.value = response.data.length
    } else {
      ElMessage.error('获取用户列表失败')
    }
  } catch (error) {
    ElMessage.error('获取用户列表失败：' + error.message)
  } finally {
    loading.value = false
  }
}

// 查看用户详情
const handleView = (user) => {
  dialogType.value = 'view'
  currentUser.value = { ...user }
  dialogVisible.value = true
}

// 编辑用户
const handleEdit = (user) => {
  dialogType.value = 'edit'
  currentUser.value = { ...user }
  dialogVisible.value = true
}

// 保存用户信息
const handleSave = async () => {
  if (!userForm.value) return
  
  await userForm.value.validate(async (valid) => {
    if (valid) {
      try {
        console.log(currentUser.value)
        const response = await updateUser(currentUser.value)
        if (response.status === 200) {
          ElMessage.success('更新成功')
          dialogVisible.value = false
          fetchUsers() // 刷新用户列表
        } else {
          ElMessage.error(response.data.message || '更新失败')
        }
      } catch (error) {
        ElMessage.error('更新失败：' + error.message)
      }
    }
  })
}

// 搜索处理
const handleSearch = () => {
  currentPage.value = 1;
  const query = searchQuery.value.toLowerCase();
  users.value = allUsers.value.filter(user =>
    String(user.username).toLowerCase().includes(query) ||
    String(user.email).toLowerCase().includes(query) ||
    String(user.role).toLowerCase().includes(query) ||
    String(user.gender).toLowerCase().includes(query)
  );
}

// 分页处理
const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
}

const handleCurrentChange = (val) => {
  currentPage.value = val
}

// 日期格式化
const formatDate = (date) => {
  if (!date) return ''
  return new Date(date).toLocaleString()
}

// 初始化
onMounted(() => {
  fetchUsers()
})
</script>

<style scoped>
.user-management {
  padding: 20px;
}

.user-card {
  margin-left: 80px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.card-header h2 {
  margin: 0;
  color: #1e293b;
  font-size: 1.5rem;
}

.search-input {
  width: 300px;
}

.search-input :deep(.el-input__wrapper) {
  border-radius: 20px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

:deep(.el-table) {
  border-radius: 8px;
  overflow: hidden;
}

:deep(.el-table th) {
  background-color: #f8fafc;
  color: #1e293b;
  font-weight: 600;
}

:deep(.el-table td) {
  color: #475569;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

:deep(.el-form-item__label) {
  font-weight: 500;
  color: #1e293b;
}

:deep(.el-input__wrapper) {
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
}

:deep(.el-input__wrapper:hover) {
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

:deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.2);
}

@media (max-width: 768px) {
  .card-header {
    flex-direction: column;
    gap: 15px;
  }

  .search-input {
    width: 100%;
  }

  .pagination-container {
    justify-content: center;
  }
}
</style> 
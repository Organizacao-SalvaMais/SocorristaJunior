/*package com.example.socorristajunior.ui.screens.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EmergencyContactsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(EmergencyContactsState())
    val uiState: StateFlow<EmergencyContactsState> = _uiState.asStateFlow()

    init {
        loadEmergencyContacts()
    }

    private fun loadEmergencyContacts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            try {
                // Simula delay de rede
                kotlinx.coroutines.delay(500)

                val contacts = getEmergencyContacts()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    contacts = contacts
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Erro ao carregar contatos: ${e.message}"
                )
            }
        }
    }

    fun onContactClick(phoneNumber: String) {
        viewModelScope.launch {
            // Aqui você pode adicionar analytics ou lógica adicional
            _uiState.value = _uiState.value.copy(
                lastDialedNumber = phoneNumber
            )
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    private fun getEmergencyContacts(): List<EmergencyContact> {
        return listOf(
            EmergencyContact(
                name = "SAMU",
                phone = "192",
                icon = Icons.Default.LocalHospital,
                iconColor = Color.White,
                backgroundColor = Color(0xFFE62727),
                description = "Serviço de Atendimento Móvel de Urgência - Atendimento médico de emergência"
            ),
            EmergencyContact(
                name = "BOMBEIROS",
                phone = "193",
                icon = Icons.Default.LocalFireDepartment,
                iconColor = Color.White,
                backgroundColor = Color(0xFFA40000),
                description = "Corpo de Bombeiros Militar - Atendimento a incêndios e resgates"
            ),
            EmergencyContact(
                name = "POLÍCIA MILITAR",
                phone = "190",
                icon = Icons.Default.Security,
                iconColor = Color.White,
                backgroundColor = Color(0xFF517BCA),
                description = "Polícia Militar - Segurança pública e emergências policiais"
            ),
            EmergencyContact(
                name = "POLÍCIA CIVIL",
                phone = "181",
                icon = Icons.Default.Security,
                iconColor = Color.White,
                backgroundColor = Color(0xFF6B7280),
                description = "Polícia Civil - Investigações criminais e registros de ocorrência"
            ),
            EmergencyContact(
                name = "DEFESA CIVIL",
                phone = "199",
                icon = Icons.Default.Public,
                iconColor = Color.White,
                backgroundColor = Color(0xFFD7B681),
                description = "Defesa Civil - Prevenção e atendimento a desastres naturais"
            )
        )
    }
}

data class EmergencyContactsState(
    val contacts: List<EmergencyContact> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val lastDialedNumber: String? = null
)

data class EmergencyContact(
    val name: String,
    val phone: String,
    val icon: ImageVector,
    val iconColor: Color,
    val backgroundColor: Color,
    val description: String
)

 */
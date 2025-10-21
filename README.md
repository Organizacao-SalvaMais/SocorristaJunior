# SocorristaJunior

Descrição do Projeto

## Estrutura do Projeto

C:. | estrutura_completa (com_arquivos).txt | GerarEstrutura.bat | GerarEstrutura.txt | MainActivity.kt | SocorristaJuniorApp.kt |

+---Data | +---BancoDeDados | | AppDatabase.kt | | PrepopulateDatabaseCallback.kt | | UserDatabase.kt | |

| +---DAO | | EmergenciaDAO.kt | | PassoDAO.kt | | UserDAO.kt | |

| ---model | EmergenciaModel.kt | JsonEmerModels.kt | Mappers.kt | PassoModel.kt | UserEntity.kt |

+---di | AppModule.kt | DatabaseModule.kt |

+---Domain | +---CasosDeUso | | GetPassos.kt | |

| ---Repositorio | CadastroRepositorio.kt | EmergenciaRepo.kt | PassoRepo.kt |

---ui +---components | BarraDePesquisa.kt | BotaoDeNumerosEmergencia.kt | BotaoEmergencia.kt | EmergencyCard.kt | EmergencyDetailContent.kt | FeatureCard.kt | Label.kt |

+---screens | +---cadastro | | CadastroActivity.kt | | CadastroScreen.kt | | CadastroViewModel.kt | |

| +---details | | DetailsScreen.kt | | DetailsViewModel.kt | |

| +---editProfile | | EditProfileScreen.kt | |

| +---emergencies | | EmergenciesScreen.kt | | EmergenciesViewModel.kt | |

| +---home | | HomeScreen.kt | | HomeViewModel.kt | |

| +---login | | LoginScreen.kt | | LoginViewModel.kt | |

| +---Profile | | ProfileScreen.kt | | ProfileViewModel.kt | |

| ---quiz | QuizScreen.kt | QuizViewModel.kt |

---theme Color.kt Theme.kt Type.kt

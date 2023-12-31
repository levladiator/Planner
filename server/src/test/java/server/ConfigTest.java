package server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import server.database.*;
import server.helpers.TestAuthService;
import server.helpers.TestSimpMessaging;
import server.helpers.TestSocketRefresher;
import server.services.*;

@TestConfiguration
public class ConfigTest {
    @Autowired
    private ApplicationContext context;

    @Bean
    @Primary
    public SocketRefreshService socketRefreshServiceMock() {
        return new TestSocketRefresher();
    }

    @Bean
    public TestBoardsRepository testBoardsRepository() {
        return (TestBoardsRepository) context.getBean(BoardRepository.class);
    }

    @Bean
    public TestAuthService testAuthService() {
        return (TestAuthService) context.getBean(RepositoryBasedAuthService.class);
    }

    @Bean
    public TestUserRepository testUserRepository() {
        return (TestUserRepository) context.getBean(UserRepository.class);
    }

    @Bean
    public TestCardRepository testCardRepository() {
        return (TestCardRepository) context.getBean(CardRepository.class);
    }

    @Bean
    public TestCardListRepository testCardListRepository() {
        return (TestCardListRepository) context.getBean(CardListRepository.class);
    }

    @Bean
    public TestTaskRepository testTaskRepository() {
        return (TestTaskRepository) context.getBean(TaskRepository.class);
    }

    @Bean
    public TestTagRepository testTagRepository() {
        return (TestTagRepository) context.getBean(TagRepository.class);
    }

    @Bean
    public TestColorPresetRepository testColorPresetRepository() {
        return (TestColorPresetRepository) context.getBean(ColorPresetRepository.class);
    }

    @Bean
    @Primary
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public UserRepository userRepositoryMock() {
        return new TestUserRepository();
    }

    @Bean
    @Primary
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public RepositoryBasedAuthService repositoryBasedAuthServiceMock() {
        return new TestAuthService();
    }

    @Bean
    public TestSimpMessaging testSimpMessaging() {
        return (TestSimpMessaging) context.getBean(SimpMessagingTemplate.class);
    }

    @Bean
    @Primary
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public SimpMessagingTemplate simpMessagingTemplateMock() {
        return new TestSimpMessaging();
    }

    @Bean
    @Primary
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public BoardRepository boardRepositoryMock() {
        return new TestBoardsRepository();
    }

    @Bean
    @Primary
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public CardRepository cardRepositoryMock() {
        return new TestCardRepository();
    }

    @Bean
    @Primary
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public CardListRepository cardListRepositoryMock() {
        return new TestCardListRepository();
    }

    @Bean
    @Primary
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public TaskRepository taskRepositoryMock() {
        return new TestTaskRepository();
    }

    @Bean
    @Primary
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public TagRepository tagRepositoryMock() {
        return new TestTagRepository();
    }

    @Bean
    @Primary
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public ColorPresetRepository colorPresetRepositoryMock() {
        return new TestColorPresetRepository();
    }
}

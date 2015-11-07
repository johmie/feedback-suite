package io.feedback.core.repository;

import io.feedback.core.entity.AbstractEntity;
import io.feedback.wrapper.org.springframework.core.GenericTypeResolver;
import junitparams.JUnitParamsRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnitParamsRunner.class)
@ContextConfiguration(locations = {"/test-spring-config.xml"})
public class AbstractRepositoryTest {

    private AbstractRepository abstractRepository;

    @Before
    public void setUp() {
        abstractRepository = mock(AbstractRepository.class, Mockito.CALLS_REAL_METHODS);
        abstractRepository.setEntityManager(mock(EntityManager.class));
        abstractRepository.setGenericTypeResolver(mock(GenericTypeResolver.class));
    }

    @Test
    public void getAndSetEntityManagerWorks() {
        EntityManager entityManagerMock = mock(EntityManager.class);
        abstractRepository.setEntityManager(entityManagerMock);
        assertEquals(entityManagerMock, abstractRepository.getEntityManager());
    }

    @Test
    public void getAndSetGenericTypeResolverWorks() {
        GenericTypeResolver genericTypeResolver = mock(GenericTypeResolver.class);
        abstractRepository.setGenericTypeResolver(genericTypeResolver);
        assertEquals(genericTypeResolver, abstractRepository.getGenericTypeResolver());
    }

    @Test
    public void findByIdReturnsEntity() {
        AbstractEntity entityMock = mock(AbstractEntity.class);
        when(abstractRepository.getEntityManager().find(this.anyClass(), eq(1L))).thenReturn(entityMock);
        AbstractEntity entity = abstractRepository.findById(1L);
        assertEquals(entityMock, entity);
    }

    @Test
    public void insertOrUpdateEntityWithoutIdentifierCallsPersistOfEntityManager() {
        AbstractEntity entityMock = mock(AbstractEntity.class);
        when(entityMock.getId()).thenReturn(null);
        abstractRepository.insertOrUpdate(entityMock);
        verify(abstractRepository.getEntityManager()).persist(entityMock);
    }

    @Test
    public void insertOrUpdateEntityWithIdentifierCallsMergeOfEntityManager() {
        AbstractEntity entityMock = mock(AbstractEntity.class);
        when(entityMock.getId()).thenReturn(1L);
        abstractRepository.insertOrUpdate(entityMock);
        verify(abstractRepository.getEntityManager()).merge(entityMock);
    }

    @Test
    public void deleteCallsRemoveMethodOfEntityManager() {
        AbstractEntity entityMock = mock(AbstractEntity.class);
        abstractRepository.delete(entityMock);
        verify(abstractRepository.getEntityManager()).remove(entityMock);
    }

    private Class<?> anyClass() {
        return argThat(new AnyClassMatcher());
    }

    private class AnyClassMatcher implements ArgumentMatcher<Class<?>> {

        @Override
        public boolean matches(final Object argument) {
            return true;
        }
    }
}
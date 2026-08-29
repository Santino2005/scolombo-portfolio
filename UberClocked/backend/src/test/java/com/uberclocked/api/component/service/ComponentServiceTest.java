package com.uberclocked.api.component.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.uberclocked.api.common.exceptions.ResourceAlreadyExistsException;
import com.uberclocked.api.common.exceptions.ResourceDoesNotExistsException;
import com.uberclocked.api.component.mapper.ComponentMapper;
import com.uberclocked.api.component.model.dto.ComponentDto;
import com.uberclocked.api.component.model.dto.UpdateComponentDto;
import com.uberclocked.api.component.model.entity.Component;
import com.uberclocked.api.component.repository.ComponentRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ComponentServiceTest {

  @Mock private ComponentRepository repository;
  @Mock private ComponentMapper mapper;

  private ComponentService componentService;

  @BeforeEach
  void setUp() {
    componentService = new ComponentService(repository, mapper);
  }

  @Test
  void create_whenNotExists_createsAndReturnsDto() {
    ComponentDto dto = new ComponentDto("GPU", "Graphics Card", new HashMap<>());
    Component entity = new Component("GPU", "Graphics Card");

    when(repository.existsBySkuPrefix("GPU")).thenReturn(false);
    when(mapper.toEntity(dto)).thenReturn(entity);
    when(repository.save(entity)).thenReturn(entity);
    when(mapper.toDto(entity)).thenReturn(dto);

    ComponentDto result = componentService.create(dto);

    assertNotNull(result);
    assertEquals("GPU", result.skuPrefix());
  }

  @Test
  void create_whenExists_throwsResourceAlreadyExistsException() {
    ComponentDto dto = new ComponentDto("GPU", "Graphics Card", new HashMap<>());
    when(repository.existsBySkuPrefix("GPU")).thenReturn(true);

    assertThrows(ResourceAlreadyExistsException.class, () -> componentService.create(dto));
  }

  @Test
  void update_whenExists_updatesAndReturnsDto() {
    UpdateComponentDto updateDto = new UpdateComponentDto("New Name", new HashMap<>());
    Component entity = new Component("GPU", "Graphics Card");
    ComponentDto dto = new ComponentDto("GPU", "New Name", new HashMap<>());

    when(repository.existsBySkuPrefix("GPU")).thenReturn(true);
    when(repository.getReferenceById("GPU")).thenReturn(entity);
    when(mapper.toDto(entity)).thenReturn(dto);

    ComponentDto result = componentService.update(updateDto, "GPU");

    assertEquals("New Name", result.displayName());
    verify(mapper).update(updateDto, entity);
  }

  @Test
  void update_whenNotExists_throwsResourceDoesNotExistsException() {
    UpdateComponentDto updateDto = new UpdateComponentDto("New Name", new HashMap<>());
    when(repository.existsBySkuPrefix("GPU")).thenReturn(false);

    assertThrows(
        ResourceDoesNotExistsException.class, () -> componentService.update(updateDto, "GPU"));
  }

  @Test
  void delete_whenExists_deletes() {
    when(repository.existsBySkuPrefix("GPU")).thenReturn(true);

    componentService.delete("GPU");

    verify(repository).deleteById("GPU");
  }

  @Test
  void getEntityById_whenFound_returnsEntity() {
    Component entity = new Component("GPU", "Graphics Card");
    when(repository.findById("GPU")).thenReturn(Optional.of(entity));

    Component result = componentService.getEntityById("GPU");

    assertEquals(entity, result);
  }

  @Test
  void getEntityById_whenNotFound_throwsException() {
    when(repository.findById("GPU")).thenReturn(Optional.empty());

    assertThrows(ResourceDoesNotExistsException.class, () -> componentService.getEntityById("GPU"));
  }

  @Test
  void exists_returnsBoolean() {
    when(repository.existsBySkuPrefix("GPU")).thenReturn(true);
    assertTrue(componentService.exists("GPU"));
  }

  @Test
  void getAll_returnsList() {
    Component entity = new Component("GPU", "Graphics Card");
    ComponentDto dto = new ComponentDto("GPU", "Graphics Card", new HashMap<>());

    when(repository.findAll()).thenReturn(List.of(entity));
    when(mapper.toDto(entity)).thenReturn(dto);

    List<ComponentDto> list = componentService.getAll();

    assertEquals(1, list.size());
  }

  @Test
  void getOne_whenFound_returnsDto() {
    Component entity = new Component("GPU", "Graphics Card");
    ComponentDto dto = new ComponentDto("GPU", "Graphics Card", new HashMap<>());

    when(repository.existsBySkuPrefix("GPU")).thenReturn(true);
    when(repository.getReferenceById("GPU")).thenReturn(entity);
    when(mapper.toDto(entity)).thenReturn(dto);

    ComponentDto result = componentService.getOne("GPU");

    assertEquals("GPU", result.skuPrefix());
  }
}

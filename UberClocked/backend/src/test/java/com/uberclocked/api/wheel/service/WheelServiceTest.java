package com.uberclocked.api.wheel.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.uberclocked.api.promotion.service.PromotionService;
import com.uberclocked.api.wheel.model.dto.WheelDto;
import com.uberclocked.api.wheel.model.entity.WheelSpinEntity;
import com.uberclocked.api.wheel.repository.WheelSpinRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WheelServiceTest {

  @Mock private WheelSpinRepository wheelSpinRepo;
  @Mock private PromotionService promotionService;

  private WheelService wheelService;
  private final ZoneId zone = ZoneId.of("UTC");

  @BeforeEach
  void setUp() {
    wheelService = new WheelService(wheelSpinRepo, promotionService);
  }

  @Test
  void status_whenNotSpunToday_returnsCanSpinTrue() {
    when(wheelSpinRepo.findByUserIdAndSpinDate(eq("user1"), any(LocalDate.class)))
        .thenReturn(Optional.empty());

    WheelDto.WheelStatusResponse response = wheelService.status("user1", zone);

    assertTrue(response.canSpin);
    assertNull(response.nextSpinAt);
    assertNull(response.secondsRemaining);
  }

  @Test
  void status_whenAlreadySpunToday_returnsCanSpinFalse() {
    WheelSpinEntity spin = new WheelSpinEntity();
    when(wheelSpinRepo.findByUserIdAndSpinDate(eq("user1"), any(LocalDate.class)))
        .thenReturn(Optional.of(spin));

    WheelDto.WheelStatusResponse response = wheelService.status("user1", zone);

    assertFalse(response.canSpin);
    assertNotNull(response.nextSpinAt);
    assertNotNull(response.secondsRemaining);
  }

  @Test
  void spin_whenAlreadySpun_denies() {
    WheelSpinEntity spin = new WheelSpinEntity();
    when(wheelSpinRepo.findByUserIdAndSpinDate(eq("user1"), any(LocalDate.class)))
        .thenReturn(Optional.of(spin));

    WheelDto.WheelSpinResponse response = wheelService.spin("user1", zone);

    assertFalse(response.canSpin);
    assertNotNull(response.nextSpinAt);
  }

  @Test
  void spin_whenNotSpun_givesPrizeAndSaves() {
    when(wheelSpinRepo.findByUserIdAndSpinDate(eq("user1"), any(LocalDate.class)))
        .thenReturn(Optional.empty());

    WheelDto.PromotionDto promoDto = new WheelDto.PromotionDto();
    promoDto.id = UUID.randomUUID();

    when(promotionService.createWheelPromotion(eq("user1"), any(), any(Integer.class), any()))
        .thenReturn(promoDto);

    WheelDto.WheelSpinResponse response = wheelService.spin("user1", zone);

    assertTrue(response.canSpin);
    assertNotNull(response.prize);
    assertNotNull(response.promotion);
    verify(wheelSpinRepo).save(any(WheelSpinEntity.class));
  }
}

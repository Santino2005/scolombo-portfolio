package com.uberclocked.api.wheel.service;

import com.uberclocked.api.promotion.service.PromotionService;
import com.uberclocked.api.wheel.model.dto.WheelDto;
import com.uberclocked.api.wheel.model.entity.WheelSpinEntity;
import com.uberclocked.api.wheel.repository.WheelSpinRepository;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WheelService {

  private final WheelSpinRepository wheelSpinRepo;
  private final PromotionService promotionService;

  private final SecureRandom rng = new SecureRandom();

  public WheelService(WheelSpinRepository wheelSpinRepo, PromotionService promotionService) {
    this.wheelSpinRepo = wheelSpinRepo;
    this.promotionService = promotionService;
  }

  public WheelDto.WheelStatusResponse status(String userId, ZoneId zone) {

    LocalDate today = LocalDate.now(zone);
    boolean alreadySpun = wheelSpinRepo.findByUserIdAndSpinDate(userId, today).isPresent();

    var res = new WheelDto.WheelStatusResponse();
    res.canSpin = !alreadySpun;

    if (!res.canSpin) {
      Instant next = tomorrowStart(zone);
      res.nextSpinAt = next;
      res.secondsRemaining = Duration.between(Instant.now(), next).getSeconds();
    } else {
      res.nextSpinAt = null;
      res.secondsRemaining = null;
    }

    return res;
  }

  @Transactional
  public WheelDto.WheelSpinResponse spin(String userId, ZoneId zone) {
    var today = LocalDate.now(zone);

    if (wheelSpinRepo.findByUserIdAndSpinDate(userId, today).isPresent()) {
      var denied = new WheelDto.WheelSpinResponse();
      denied.canSpin = false;
      denied.nextSpinAt = tomorrowStart(zone);
      return denied;
    }

    var prize = pickPrize();

    var promo =
        promotionService.createWheelPromotion(userId, prize.label, prize.discount, prize.targets);

    var spin = new WheelSpinEntity();
    spin.setUserId(userId);
    spin.setSpinDate(today);
    spin.setPrizeLabel(prize.label);
    spin.setDiscount(prize.discount);
    spin.setPromotionId(promo.id);
    wheelSpinRepo.save(spin);

    var res = new WheelDto.WheelSpinResponse();

    res.canSpin = true;
    res.nextSpinAt = null;
    res.prize = prize;
    res.promotion = promo;
    return res;
  }

  public record PrizeConfig(String label, int discount, int weight) {}

  public static final List<PrizeConfig> PRIZE_POOL =
      List.of(
          new PrizeConfig("5% OFF", 5, 35),
          new PrizeConfig("10% OFF", 10, 25),
          new PrizeConfig("15% OFF", 15, 20),
          new PrizeConfig("20% OFF", 20, 12),
          new PrizeConfig("25% OFF", 25, 6),
          new PrizeConfig("50% OFF", 50, 2));

  public List<WheelDto.WheelPrizeDto> getPrizes() {
    return PRIZE_POOL.stream()
        .map(
            p -> {
              var dto = new WheelDto.WheelPrizeDto();
              dto.label = p.label();
              dto.discount = p.discount();
              dto.targets = List.of();
              return dto;
            })
        .toList();
  }

  private Instant tomorrowStart(ZoneId zone) {
    return LocalDate.now(zone).plusDays(1).atStartOfDay(zone).toInstant();
  }

  private WheelDto.WheelPrizeDto pickPrize() {
    int total = PRIZE_POOL.stream().mapToInt(PrizeConfig::weight).sum();
    int r = rng.nextInt(total);

    PrizeConfig chosen = PRIZE_POOL.get(0);
    int acc = 0;
    for (var p : PRIZE_POOL) {
      acc += p.weight();
      if (r < acc) {
        chosen = p;
        break;
      }
    }

    var dto = new WheelDto.WheelPrizeDto();
    dto.label = chosen.label();
    dto.discount = chosen.discount();
    dto.targets = List.of();
    return dto;
  }
}

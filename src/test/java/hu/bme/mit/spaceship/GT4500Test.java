package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class GT4500Test {

  private GT4500 ship;
  private TorpedoStore mockPrimaryTS;
  private TorpedoStore mockSecondaryTS;

  @BeforeEach
  public void init() {
    mockPrimaryTS = mock(TorpedoStore.class);
    mockSecondaryTS = mock(TorpedoStore.class);
    this.ship = new GT4500(mockPrimaryTS, mockSecondaryTS);
  }

  @Test
  public void fireTorpedo_Single_Success() {

    // Arrange
    when(mockPrimaryTS.fire(1)).thenReturn(true);

    // Act
    this.ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(mockPrimaryTS, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_All_Success() {
    // Arrange
    when(mockPrimaryTS.fire(1)).thenReturn(true);
    when(mockSecondaryTS.fire(1)).thenReturn(true);

    // Act
    this.ship.fireTorpedo(FiringMode.ALL);

    // Assert
    verify(mockPrimaryTS, times(1)).fire(1);
    verify(mockSecondaryTS, times(1)).fire(1);

  }

  @Test
  public void alternate_TorpedoStores() {
    // Arrange
    when(mockPrimaryTS.fire(1)).thenReturn(true);
    when(mockSecondaryTS.fire(1)).thenReturn(true);

    // Act
    this.ship.fireTorpedo(FiringMode.SINGLE);
    this.ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(mockPrimaryTS, times(1)).fire(1);
    verify(mockSecondaryTS, times(1)).fire(1);
  }

  @Test
  public void dont_fire_if_TorpedoStores_empty() {
    // Arrange
    when(mockPrimaryTS.isEmpty()).thenReturn(true);
    when(mockSecondaryTS.isEmpty()).thenReturn(true);

    // Act
    this.ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(mockPrimaryTS, times(0)).fire(1);
    verify(mockSecondaryTS, times(0)).fire(1);

  }

  @Test
  public void use_secondary_TorpedoStore_if_first_empty() {
    // Arrange
    when(mockPrimaryTS.isEmpty()).thenReturn(true);
    when(mockSecondaryTS.fire(1)).thenReturn(true);

    // Act
    this.ship.fireTorpedo(FiringMode.SINGLE);
    this.ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(mockPrimaryTS, times(0)).fire(1);
    verify(mockSecondaryTS, times(2)).fire(1);
  }

  @Test
  public void dont_fire_if_TorpedoStore_report_failure() {
    // Arrange
    when(mockPrimaryTS.fire(1)).thenReturn(false);

    // Act
    this.ship.fireTorpedo(FiringMode.SINGLE);
    boolean result = this.ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(result, false);
  }

  @Test
  public void check_fireTorpedo_return_value() {
    // Arrange
    when(mockPrimaryTS.fire(1)).thenReturn(false);
    when(mockSecondaryTS.fire(1)).thenReturn(true);

    // Act
    boolean result = this.ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(result, true);
  }

  @Test
  public void fire_first_Torpedo_store_if_secondory_empty() {

    // Arrange
    when(mockSecondaryTS.isEmpty()).thenReturn(true);

    // Act
    this.ship.fireTorpedo(FiringMode.SINGLE);
    this.ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(mockPrimaryTS, times(2)).fire(1);
  }

  @Test
  public void check_fireLaser() {
    // Act
    boolean result = this.ship.fireLaser(FiringMode.SINGLE);

    assertEquals(result, false);
  }

}

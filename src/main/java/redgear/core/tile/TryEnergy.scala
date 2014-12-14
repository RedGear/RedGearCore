package redgear.core.tile

trait TryEnergy {
  
  /**
   * If the amount of energy requested is found, then use it and return true,
   * otherwise do nothing and return false
   *
   * @param energy Amount of energy needed to work
   * @return true if there is enough power, false if there is not
   */
  protected def tryUseEnergy(energy: Int): Boolean

}
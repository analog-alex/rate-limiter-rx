export function scaleIntervalByUnit(interval: number, unit: string): number {
  let milis = 1;
  switch (unit) {
    case "sec": {
      milis = interval * 1000;
      break;
    }
    case "min": {
      milis = interval * 60 * 1000;
      break;
    }
    case "hour": {
      milis = interval * 60 * 60 * 1000;
      break;
    }
    default: {
      break;
    }
  }

  return milis;
}

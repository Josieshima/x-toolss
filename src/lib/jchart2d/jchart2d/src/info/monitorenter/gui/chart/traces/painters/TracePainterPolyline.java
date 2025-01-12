/*
 *  TracePainterFill.java,  <enter purpose here>.
 *  Copyright (c) 2004 - 2010  Achim Westermann, Achim.Westermann@gmx.de
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 * 
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *  If you modify or optimize the code in a useful way please let me know.
 *  Achim.Westermann@gmx.de
 */
package info.monitorenter.gui.chart.traces.painters;

import info.monitorenter.gui.chart.ITracePoint2D;

import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;

/**
 * A trace painter that increases performance by summing up all points to render
 * for a paint iteration (submitted by
 * {@link #paintPoint(int, int, int, int, Graphics, ITracePoint2D)} invocations
 * between {@link #startPaintIteration(Graphics)} and
 * {@link #endPaintIteration(Graphics)}) and only invoking only one polyline
 * paint for a paint call of the corresponding
 * {@link info.monitorenter.gui.chart.Chart2D}.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * @version $Revision: 1.20 $
 */
public class TracePainterPolyline extends ATracePainter {

  /** Generated <code>serialVersionUID</code>. */
  private static final long serialVersionUID = 142122979535173974L;

  /** The list of x coordinates collected in one paint iteration. */
  private List<Integer> m_xPoints;

  /** The list of y coordinates collected in one paint iteration. */
  private List<Integer> m_yPoints;

  /**
   * Default Constructor.
   * <p>
   */
  public TracePainterPolyline() {
    // nop
  }

  /**
   * @see info.monitorenter.gui.chart.ITracePainter#endPaintIteration(java.awt.Graphics)
   */
  @Override
  public void endPaintIteration(final Graphics g2d) {
    if (g2d != null) {

      int[] x = new int[this.m_xPoints.size() + 1];
      int count = 0;
      for (Integer xpoint : this.m_xPoints) {
        x[count] = xpoint.intValue();
        count++;
      }
      x[count] = this.getPreviousX();

      int[] y = new int[this.m_yPoints.size() + 1];
      count = 0;
      for (Integer ypoint : this.m_yPoints) {
        y[count] = ypoint.intValue();
        count++;
      }
      y[count] = this.getPreviousY();

      g2d.drawPolyline(x, y, x.length);
    }
  }

  /**
   * @see info.monitorenter.gui.chart.traces.painters.ATracePainter#paintPoint(int,
   *      int, int, int, java.awt.Graphics,
   *      info.monitorenter.gui.chart.ITracePoint2D)
   */
  @Override
  public void paintPoint(final int absoluteX, final int absoluteY, final int nextX,
      final int nextY, final Graphics g, final ITracePoint2D original) {
    super.paintPoint(absoluteX, absoluteY, nextX, nextY, g, original);
    this.m_xPoints.add(new Integer(absoluteX));
    this.m_yPoints.add(new Integer(absoluteY));

  }

  /**
   * @see info.monitorenter.gui.chart.ITracePainter#startPaintIteration(java.awt.Graphics)
   */
  @Override
  public void startPaintIteration(final Graphics g2d) {
    super.startPaintIteration(g2d);
    if (this.m_xPoints == null) {
      this.m_xPoints = new LinkedList<Integer>();
    } else {
      this.m_xPoints.clear();
    }
    if (this.m_yPoints == null) {
      this.m_yPoints = new LinkedList<Integer>();
    } else {
      this.m_yPoints.clear();
    }
  }

}
